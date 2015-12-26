/*
    Copyright 2015 Mark Otway

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.dotosoft.tools.quizparser.images;

import com.dotosoft.tools.quizparser.helper.TimeUtils;
import com.dotosoft.tools.quizparser.images.metadata.ImageInformation;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.Query;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaContent;
import com.google.gdata.data.photos.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;

import static com.dotosoft.tools.quizparser.images.metadata.ImageInformation.readImageInformation;
import static com.dotosoft.tools.quizparser.images.metadata.ImageInformation.safeReadImageInformation;

/**
 * This is a simple client that provides high-level operations on the Picasa Web
 * Albums GData API. It can also be used as a command-line application to test
 * out some of the features of the API.
 *
 *
 */
public class PicasawebClient {
    Logger log = Logger.getLogger(PicasawebClient.class);
    
    public static final String AUTO_BACKUP_FOLDER = "Auto Backup";
    public static final String AUTO_UPLOAD_TYPE = "InstantUploadAuto";
    public static final String INSTANT_UPLOAD = "InstantUpload";
    private static final String ALBUM_TYPE_PATTERN = "<gphoto:albumType>%s</gphoto:albumType>";
    private static final String SYNC_CLIENT_NAME = "com.dotosoft.images";
    private static final int CONNECTION_TIMEOUT_SECS = 10;

    private static final String API_PREFIX = "https://picasaweb.google.com/data/feed/api/user/";

    private final PicasawebService service = new PicasawebService(SYNC_CLIENT_NAME);;

    /**
     * Constructs a new un-authenticated client.
     */
    public PicasawebClient(Credential credential ) {
        service.setOAuth2Credentials( credential );
        service.setConnectTimeout( 1000 * CONNECTION_TIMEOUT_SECS );
        service.setReadTimeout(1000 * CONNECTION_TIMEOUT_SECS);
    }

    /**
     * Constructs a new client with the given username and password.
     */
    public PicasawebClient(String uname, String passwd) {

        log.info("Logging into Picasa Service...");

        if (uname != null && passwd != null) {
            try {
                service.setUserCredentials(uname, passwd);
            } catch (AuthenticationException e) {
                throw new IllegalArgumentException(
                        "Authentication failed. Illegal username/password combination.");
            }
        }
        else {
            throw new IllegalArgumentException(
                    "Authentication failed. User/pass not set.");
        }
    }

    public boolean downloadPhoto(File saveLocation, PhotoEntry photo) throws IOException, ParseException
    {
        boolean downloadSuccess = false;
        final int BUFFER_SIZE = 8096;
        final int TIMEOUT_MS = 10 * 1000;

        File saveFolder = saveLocation.getParentFile();
        boolean createdFolder = false;

        if( ! saveFolder.exists() )
        {
            log.info("Creating local folder: " + saveFolder.getName());
            if (!saveFolder.mkdirs())
                throw new IOException("Unable to create folder " + saveFolder.getName());
            createdFolder = true;
        }


        log.debug("Beginning download for " + saveLocation + "...");

        File tempFile = new File(saveLocation + ".tmp");
        tempFile.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(tempFile);

        List<MediaContent> media = photo.getMediaContents();
        URL fileUrl = new URL(photo.getMediaContents().get(0).getUrl());

        if( media.size() > 1 ){
            if( media.size() > 2 ){
                log.debug( "Extracting h264 video stream...");
                fileUrl = new URL(photo.getMediaContents().get(2).getUrl());
            }
            else {
                log.debug( "Extracting low-res video stream...");
                fileUrl = new URL(photo.getMediaContents().get(1).getUrl());
            }
        }

        try {
            URLConnection conn = fileUrl.openConnection();
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);
            InputStream dis = conn.getInputStream();

            int totalRead = 0;
            int readCount = 0;
            byte b[] = new byte[BUFFER_SIZE];
            while ((readCount = dis.read(b)) != 0 && readCount != -1) {
                totalRead += readCount;
                fos.write(b, 0, readCount);
            }
            dis.close();
            fos.close();

            if (!tempFile.renameTo(saveLocation))
                throw new IOException("Unable to rename temp file to " + saveLocation);

            // Fix up the timestamps from the photo metadata
            updateTimeFromTags(saveLocation, photo, createdFolder);

            log.info("Written " + FileUtils.byteCountToDisplaySize(totalRead) + " to " + saveLocation + " successfully.");
            downloadSuccess = true;
        }
        catch( ConnectException ex ){
            log.warn( "Network connection downloading " + fileUrl, ex );
            saveLocation = null;
        }
        catch( Exception ex ){
            log.error( "Unexpected exception downloading " + fileUrl, ex );
            saveLocation = null;

        }

        return downloadSuccess;
    }

    public AlbumEntry prepareRemoteAlbum(AlbumEntry albumEntry) throws IOException, ServiceException {

        // See if the AlbumEntry was valid remotely (i.e., it has an ID). If not, create it
        if ( albumEntry.getId() == null )
        {
                albumEntry.setDescription(new PlainTextConstruct("Automatically created by Picasync"));
                albumEntry = insertAlbum(albumEntry);

            if( albumEntry.getId() == null || albumEntry.getId().isEmpty()) {
                log.error("Unable to create new album: " + albumEntry.getTitle().getPlainText() );
                return null;
            }
        }

        return albumEntry;
    }

    public void updateTimeFromTags( File localFile, PhotoEntry photo, boolean createdFolder )
            throws com.google.gdata.util.ParseException, IOException
    {
        ExifTags tags = photo.getExifTags();
        DateTime photoDate = null;

        if( tags != null ) {
            Date timestamp = tags.getTime();
            if( timestamp != null )
                photoDate = new DateTime(timestamp);
        }

        if( photoDate == null ){
            photoDate = photo.getUpdated();
        }

        log.debug("Setting datetime for " + localFile.getName() + " to " + photoDate.toString());

        Path fp = Paths.get( localFile.getPath() );
        FileTime time = FileTime.fromMillis( photoDate.getValue() );
        Files.setAttribute(fp, "basic:creationTime", time );

        long lastUpdated = photo.getUpdated().getValue();

        // Set the last update time of the local file...
        log.debug("Setting last update datetime for " + localFile.getName() + " to " + photo.getUpdated());
        if( ! localFile.setLastModified(lastUpdated) )
            log.warn("Unable to set date/time stamp for file: " + localFile );
    }

    public static boolean isInstantUpload( AlbumEntry album )
    {
        return isAlbumOfType( AUTO_UPLOAD_TYPE, album ) ||
                isAlbumOfType( INSTANT_UPLOAD, album );
    }

    private static boolean isAlbumOfType( String type, AlbumEntry album ){
        String albumType = String.format( ALBUM_TYPE_PATTERN, type);

        XmlBlob blob = album.getXmlBlob();

        if( blob != null ) {
            String x = blob.getBlob();

            if( x != null && x.equals( albumType ) ) {
                return true;
            }
        }
        return false;
    }


    public GphotoEntry uploadImageToAlbum(File imageFile, PhotoEntry remotePhoto, GphotoEntry albumEntry, String localMd5CheckSum ) throws IOException, ServiceException {

        boolean newPhoto = false;
        String albumName = albumEntry.getTitle().getPlainText();
        PhotoEntry myPhoto = remotePhoto;
        
        if( myPhoto == null )
        {
            newPhoto = true;
            log.info( "Uploading new image to album " + albumName + ": " + imageFile);

            myPhoto = new PhotoEntry();
        }
        else{
            log.info( "Uploading updated image in album " + albumName + ": " + imageFile);
            List<MediaContent> media = myPhoto.getMediaContents();
            media.remove(0);
        }


        try{
            MediaFileSource myMedia = new MediaFileSource(imageFile, "image/jpeg");
            myPhoto.setMediaSource(myMedia);
            myPhoto.setChecksum( localMd5CheckSum );
            myPhoto.setAlbumAccess(GphotoAccess.Value.PUBLIC);
            myPhoto.setClient(SYNC_CLIENT_NAME);

            if( newPhoto)
            {
                myPhoto.setTitle(new PlainTextConstruct(imageFile.getName()));
                myPhoto = insert(albumEntry, myPhoto);
            }
            else
            {
                myPhoto = myPhoto.updateMedia(true);
            }
        } catch (Exception ex) {
            log.error("Unable to add media: " + imageFile + ": " + ex);
        }

        setUpdatedDate( albumEntry, myPhoto, imageFile );
        
        return myPhoto;
    }

    public void setAlbumDateFromFolder(File folder, AlbumEntry albumEntry)
    {
        // Can't do this for autobackup albums
        if( isAlbumOfType( AUTO_UPLOAD_TYPE, albumEntry ))
            return;

        File[] files = folder.listFiles();
        Date newestDate = null;

        if( files != null )
        {
            for (File file : files)
            {
                ImageInformation info = safeReadImageInformation(file);

                if( info != null )
                {
                    Date imageDate = info.getDateTaken();
                    if (imageDate != null)
                    {
                        if (newestDate == null || imageDate.after(newestDate))
                            newestDate = imageDate;
                    }
                }
            }

            if( newestDate != null )
            {
                log.info("Setting remote album date based on newest photo in " + folder + ": " + newestDate );


                try
                {
                    albumEntry.setDate(newestDate);
                    albumEntry.setEtag("*");
                    albumEntry.update();
                } catch (Exception ex)
                {
                    log.error("Unable to set album date for " + albumEntry, ex);
                }
            }
        }
    }
    
    public PhotoEntry createPhotoEntry (String title, String description, File fileImage) {
    	PhotoEntry myPhoto = new PhotoEntry();
    	myPhoto.setTitle(new PlainTextConstruct(title));
    	myPhoto.setDescription(new PlainTextConstruct(description));
    	myPhoto.setClient(SYNC_CLIENT_NAME);

    	MediaFileSource myMedia = new MediaFileSource(fileImage, "image/jpeg");
    	myPhoto.setMediaSource(myMedia);
    	
    	return myPhoto;
    }

    public void setUpdatedDate( GphotoEntry album, final PhotoEntry photoToChange, File localFile )
    {
        try
        {
            final boolean SETUPDATE_WORKS = false;

            if( SETUPDATE_WORKS )
            {
                List<GphotoEntry> photos = getPhotos(album);

                for (GphotoEntry photo : photos)
                {
                    if (photo.getGphotoId().equals(photoToChange.getGphotoId()))
                    {
                        DateTime time = new DateTime(localFile.lastModified());
                        time.setTzShift(0);

                        log.info( "Setting Updated from " + photo.getUpdated() + " to " + time );
                        photo.setUpdated(time);
                        photo.update();
                        break;
                    }
                }
            }
            else
            {
                // Since it doesn't work, the only option to avoid unnecessary uploads/downloads
                // is to set the lastModified file time on the local file.
                log.info("Setting local file time to " + photoToChange.getUpdated() + " for " + localFile.getName() );
                localFile.setLastModified( photoToChange.getUpdated().getValue() );
            }
        }
        catch( Exception ex )
        {
            log.error("Unable to change lastUpdate date for " + photoToChange.getTitle().getPlainText() );
        }
    }
    
//    /**
//	 * Demonstrates use of partial query to retrieve album title and location
//	 * information for user's albums.
//	 */
//	private void printAlbumLocation(String uname) throws IOException, ServiceException {
//		String albumsUrl = API_PREFIX + uname;
//		String fields = "entry(title,gphoto:id,gphoto:location)";
//
//		Query albumQuery = new Query(new URL(albumsUrl));
//		albumQuery.setFields(fields);
//
//		AlbumFeed feed = service.query(albumQuery, AlbumFeed.class);
//		for (GphotoEntry entry : feed.getEntries()) {
//			if (entry instanceof AlbumEntry) {
//				AlbumEntry albumEntry = (AlbumEntry) entry;
//				OUT.println(albumEntry.getGphotoId() + ":" + albumEntry.getTitle().getPlainText() + " ("
//						+ albumEntry.getLocation() + ")");
//			}
//		}
//	}
//    
//    /**
//	 * Demonstrates update operation using partial patch to update location
//	 * string for specified album.
//	 */
//	public void updateAlbumLocation(String uname, String albumId) throws IOException, ServiceException {
//		OUT.println("Enter album id to update:");
//		String albumId = IN.readLine();
//
//		// Get the current album entry
//		String albumEntryUrl = API_PREFIX + uname + "/" + albumId;
//		String fields = "@gd:etag,gphoto:location";
//		Query patchQuery = new Query(new URL(albumEntryUrl));
//		patchQuery.setFields(fields);
//		AlbumEntry entry = service.getEntry(patchQuery.getUrl(), AlbumEntry.class);
//		OUT.println("Current location: " + entry.getLocation());
//
//		// Update the location in the album entry
//		OUT.println("Specify new location: ");
//		String newLocation = IN.readLine();
//		entry.setLocation(newLocation);
//		entry.setSelectedFields("gphoto:location");
//		AlbumEntry updated = service.patch(new URL(albumEntryUrl), fields, entry);
//		OUT.println("Location set to: " + updated.getLocation());
//	}

    public static String getPhotoId( PhotoEntry photo )
    {
        final String PHOTOID = "photoid/";
        String id;

        int index = photo.getId().indexOf( PHOTOID );
        id = photo.getId().substring( index + PHOTOID.length() );
        return id;
    }

    public void movePhoto(PhotoEntry photo, AlbumEntry destinationAlbum) throws ServiceException, IOException
    {
        log.info("Moving photo " + photo.getTitle().getPlainText() + " to " + destinationAlbum.getTitle().getPlainText() );

        AlbumFeed feed = destinationAlbum.getFeed();
        String id = feed.getGphotoId();
        photo.setAlbumId(id );
        photo.update();
    }

    private List<UserFeed> feeds = new ArrayList<UserFeed>();
    /**
     * Retrieves the albums for the given user.
     * albumUrl = addParameter(albumUrl, "hidestreamid", "photos_from_posts" );
     */
    public List<GphotoEntry> getAlbums(String username, boolean showall ) throws IOException, ServiceException {
        
        String albumUrl = API_PREFIX + username;

        if( showall )
            albumUrl = addParameter( albumUrl, "showall", null );

        List<GphotoEntry> albums = new ArrayList<GphotoEntry>();

        while( true ) {
        	UserFeed userFeed = getFeed(albumUrl, UserFeed.class);

            feeds.add( userFeed );

            List<GphotoEntry> entries = userFeed.getEntries();

            for (GphotoEntry entry : entries) {

//                GphotoEntry adapted = entry.getAdaptedEntry();
//
//                if (adapted instanceof AlbumEntry) {
//                    AlbumEntry album = (AlbumEntry)adapted;
//                    albums.add(album);
//                }
            	
            	albums.add(entry);
            }

            Link nextLink = userFeed.getNextLink();
            if( nextLink == null )
                break;

            albumUrl = nextLink.getHref();
        }

        TimeUtils.sortGPhotoEntriesNewestFirst( albums );
        return albums;
    }

    /**
     * Retrieves the albums for the currently logged-in user.  This is equivalent
     * to calling {@link #getAlbums(String, boolean)} with "default" as the username.
     */
    public List<GphotoEntry> getAlbums( boolean showall ) throws IOException, ServiceException {
        return getAlbums("default", showall);
        // return getAlbums("113922249877693412534", showall);
    }

    /**
     * Retrieves the tags for the given user.  These are tags aggregated across
     * the entire account.
     */
    public List<TagEntry> getTags(String uname) throws IOException,
            ServiceException {
        String tagUrl = API_PREFIX + uname + "?kind=tag";
        UserFeed userFeed = getFeed(tagUrl, UserFeed.class);

        List<GphotoEntry> entries = userFeed.getEntries();
        List<TagEntry> tags = new ArrayList<TagEntry>();
        for (GphotoEntry entry : entries) {
            GphotoEntry adapted = entry.getAdaptedEntry();
            if (adapted instanceof TagEntry) {
                tags.add((TagEntry) adapted);
            }
        }
        return tags;
    }

    /**
     * Retrieves the tags for the currently logged-in user.  This is equivalent
     * to calling {@link #getTags(String)} with "default" as the username.
     */
    public List<TagEntry> getTags() throws IOException, ServiceException {
        return getTags("default");
    	// return getTags("113922249877693412534");
    }

    /**
     * Retrieves the photos for the given album.
     */
    public List<PhotoEntry> getPhotos(AlbumEntry album) throws IOException,
            ServiceException {

        List<PhotoEntry> photos = new ArrayList<PhotoEntry>();

        // If it doesn't have an ID, it's an album we haven't created yet!
        if( album.getLinks().size() != 0 ) {
            String feedHref = getLinkByRel(album.getLinks(), Link.Rel.FEED);

            feedHref = addParameter(feedHref, "imgmax", "d");
            feedHref = addParameter(feedHref, "max-results", "1000");

            while (feedHref != null) {
                AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

                List<GphotoEntry> entries = albumFeed.getEntries();
                for (GphotoEntry entry : entries) {
                    GphotoEntry adapted = entry.getAdaptedEntry();
                    if (adapted instanceof PhotoEntry) {
                        photos.add((PhotoEntry) adapted);
                    }
                }

                Link nextLink = albumFeed.getNextLink();
                if (nextLink != null) {
                    feedHref = nextLink.getHref();
                } else {
                    feedHref = null;
                }
            }
        }

        TimeUtils.sortPhotoEntriesNewestFirst(photos);

        return photos;
    }
    
    /**
     * Retrieves the photos for the given album.
     */
    public List<GphotoEntry> getPhotos(GphotoEntry photoEntry) throws IOException,
            ServiceException {

        List<GphotoEntry> photos = new ArrayList<GphotoEntry>();

        // If it doesn't have an ID, it's an album we haven't created yet!
        if( photoEntry.getLinks().size() != 0 ) {
            String feedHref = getLinkByRel(photoEntry.getLinks(), Link.Rel.FEED);

            feedHref = addParameter(feedHref, "imgmax", "d");
            feedHref = addParameter(feedHref, "max-results", "1000");

            while (feedHref != null) {
                AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

                List<GphotoEntry> entries = albumFeed.getEntries();
                for (GphotoEntry entry : entries) {
//                    GphotoEntry adapted = entry.getAdaptedEntry();
//                    if (adapted instanceof PhotoEntry) {
//                        photos.add((PhotoEntry) adapted);
//                    }
                	
                	photos.add(entry);
                }

                Link nextLink = albumFeed.getNextLink();
                if (nextLink != null) {
                    feedHref = nextLink.getHref();
                } else {
                    feedHref = null;
                }
            }
        }

        TimeUtils.sortGPhotoEntriesNewestFirst(photos);

        return photos;
    }

    /**
     * Retrieves the comments for the given photo.
     */
    public List<CommentEntry> getComments(PhotoEntry photo) throws IOException,
            ServiceException {

        String feedHref = getLinkByRel(photo.getLinks(), Link.Rel.FEED);
        AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

        List<GphotoEntry> entries = albumFeed.getEntries();
        List<CommentEntry> comments = new ArrayList<CommentEntry>();
        for (GphotoEntry entry : entries) {
            GphotoEntry adapted = entry.getAdaptedEntry();
            if (adapted instanceof CommentEntry) {
                comments.add((CommentEntry) adapted);
            }
        }
        return comments;
    }

    /**
     * Retrieves the tags for the given taggable entry.  This is valid on user,
     * album, and photo entries only.
     */
    public List<TagEntry> getTags(GphotoEntry<?> parent) throws IOException,
            ServiceException {

        String feedHref = getLinkByRel(parent.getLinks(), Link.Rel.FEED);
        feedHref = addKindParameter(feedHref, "tag");
        AlbumFeed albumFeed = getFeed(feedHref, AlbumFeed.class);

        List<GphotoEntry> entries = albumFeed.getEntries();
        List<TagEntry> tags = new ArrayList<TagEntry>();
        for (GphotoEntry entry : entries) {
            GphotoEntry adapted = entry.getAdaptedEntry();
            if (adapted instanceof TagEntry) {
                tags.add((TagEntry) adapted);
            }
        }
        return tags;
    }

    /**
     * Album-specific insert method to insert into the gallery of the current
     * user, this bypasses the need to have a top-level entry object for parent.
     */
    public AlbumEntry insertAlbum(AlbumEntry album)
            throws IOException, ServiceException {
        log.info( "Adding new album: " + album.getTitle().getPlainText() );

        String feedUrl = API_PREFIX + "default";
        return service.insert(new URL(feedUrl), album);
    }

    /**
     * Insert an entry into another entry.  Because our entries are a hierarchy,
     * this lets you insert a photo into an album even if you only have the
     * album entry and not the album feed, making it quicker to traverse the
     * hierarchy.
     */
    public <T extends GphotoEntry> T insert(GphotoEntry<?> parent, T entry)
            throws IOException, ServiceException {

        String feedUrl = getLinkByRel(parent.getLinks(), Link.Rel.FEED);

        return service.insert(new URL(feedUrl), entry);
    }


    /**
     * Insert an entry into another entry.  Because our entries are a hierarchy,
     * this lets you insert a photo into an album even if you only have the
     * album entry and not the album feed, making it quicker to traverse the
     * hierarchy.
     */
    public <T extends GphotoEntry> T update(GphotoEntry<?> parent, T entry)
            throws IOException, ServiceException {

        String feedUrl = getLinkByRel(parent.getLinks(), Link.Rel.ENTRY_EDIT);
        return service.update(new URL(feedUrl), entry);
    }

    /**
     * Helper function to allow retrieval of a feed by string url, which will
     * create the URL object for you.  Most of the Link objects have a string
     * href which must be converted into a URL by hand, this does the conversion.
     */
    public <T extends GphotoFeed> T getFeed(String feedHref, Class<T> feedClass) throws IOException, ServiceException {
        log.info("Get Feed URL: " + feedHref);
        return service.getFeed(new URL(feedHref), feedClass);
    }

    /**
     * Helper function to add a kind parameter to a url.
     */
    public String addKindParameter(String url, String kind) {

        return addParameter( url, "kind", kind );
    }

    /**
     * Helper function to add a kind parameter to a url.
     */
    public String addParameter(String url, String name, String value) {
        if (url.contains("?")) {
            url = url + "&" + name;
        } else {
            url = url + "?" + name;
        }

        if( value != null && ! value.isEmpty() )
            url = url + "=" + value;

        return url;
    }

    /**
     * Helper function to get a link by a rel value.
     */
    public String getLinkByRel(List<Link> links, String relValue) {
        for (Link link : links) {
            if (relValue.equals(link.getRel())) {
                return link.getHref();
            }
        }
        throw new IllegalArgumentException("Missing " + relValue + " link.");
    }

    public static File getFolderNameForAlbum( File rootFolder, AlbumEntry album ){
        String title = album.getTitle().getPlainText();

        if( isInstantUpload( album )) {
            // For auto backup downloads, we use the pre-defined "Auto Backup" sub-folder name and
            // force download only (uploads for auto-backup will be done separately.
            File autoBackupFolder = new File(rootFolder, AUTO_BACKUP_FOLDER);

            String localTitle = title.replace('/', '_');
            localTitle = localTitle.replace('\\', '_');

            return new File(autoBackupFolder, localTitle);
        }

        return new File( rootFolder, title );
    }

    public static String getAlbumNameForFolder( File localFolder ){

        String title = localFolder.getName();

        File parent = localFolder.getParentFile();

        if( parent.getName().equals( AUTO_BACKUP_FOLDER ) ) {

            // For auto-backup we take the name but replace underscores back with slashes
            title = title.replace('_', '/');
        }

        return title;
    }
}
