package com.dotosoft.tools.dataquizparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotosoft.tools.dataquizparser.helper.DotoQuizStructure;
import com.dotosoft.tools.dataquizparser.helper.MD5Checksum;
import com.dotosoft.tools.dataquizparser.picasa.config.Settings;
import com.dotosoft.tools.dataquizparser.picasa.syncutil.SyncState;
import com.dotosoft.tools.dataquizparser.picasa.webclient.GoogleOAuth;
import com.dotosoft.tools.dataquizparser.picasa.webclient.PicasawebClient;
import com.dotosoft.tools.dataquizparser.representations.QuestionAnswers;
import com.dotosoft.tools.dataquizparser.representations.Topics;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumData;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.util.ServiceException;

public class App {
	
	private static final Logger log = LogManager.getLogger(App.class.getName());
	
	private Settings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	private Map<String, GphotoEntry> albumMapById;
	private Map<String, GphotoEntry> albumMapByTitle;
	
	private boolean isError = false;
	
	public enum APPLICATION_TYPE {
		GENERATE_SQL, BATCH_UPLOAD
	}
	
	public App(String args[]) {
		log.info("Starting and setup Doto Parser...");
		settings = new Settings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	                webClient = auth.authenticatePicasa(settings, true, syncState );
	            }
	            catch( Exception _ex ) {
	            	isError = true;
	                log.error( "Exception while authenticating.", _ex );
	                invalidateWebClient();
	            }
	
	            if( webClient != null )
	            {
	                log.info("Connection established.");
	            }
	            else{
	                log.warn("Unable to re-authenticate. User will need to auth interactively.");
	                isError = true;
	            }
	        }
		} else {
			isError = true;
			log.error( "Error: Could not run DataQuizParser.");
			System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
		}
	}
	
	public Topics getAlbumIdByTopic(Topics topic) {
		log.info("Get topic '" + topic.getTopicName() + "'");
		try {
			if(albumMapById == null || albumMapById.isEmpty()) {
				albumMapById = new HashMap<String, GphotoEntry>();
				albumMapByTitle = new HashMap<String, GphotoEntry>();
				
				try {
					List<GphotoEntry> albumEntries = webClient.getAlbums(true);
					for(GphotoEntry album : albumEntries) {
						// System.out.println("album::: " + album);
						albumMapById.put(album.getGphotoId(), album);
						albumMapByTitle.put(album.getTitle().getPlainText(), album);
					} 
				} catch (IOException | ServiceException e) {
					e.printStackTrace();
				}
			}
			
			if(albumMapByTitle.containsKey(topic.getTopicName())) {
				GphotoEntry albumEntry = albumMapByTitle.get(topic.getTopicName());
				// Sync picture topic.png
				List<GphotoEntry> photoEntries = webClient.getPhotos(albumEntry);
				boolean isFindTopicImage = false;
				for(GphotoEntry entryPhoto : photoEntries) {
					if(entryPhoto.getTitle().getPlainText().equalsIgnoreCase("topic.png")) {
						topic.setImagePicasaUrl(((MediaContent)entryPhoto.getContent()).getUri());
						isFindTopicImage = true;
						break;
					}
				}
				
				if( !isFindTopicImage ) {
					log.info("there is no topic image at '" + topic.getTopicName() + "'. Wait for uploading...");
					java.nio.file.Path topicImagePath = java.nio.file.Paths.get(settings.getSyncDataFolder(), topic.getTopicName(), "topic.png");
					if(!topicImagePath.toFile().exists()) {
						log.error("File is not found at '" + topicImagePath.toString() + "'. Please put the file and start this app again.");
						System.exit(1);
					}
					webClient.uploadImageToAlbum(topicImagePath.toFile(), null, albumEntry, MD5Checksum.getMD5Checksum(topicImagePath.toString()));
					// clear album
					albumMapById.clear();
					albumMapByTitle.clear();
					// recursive getAlbums
					return getAlbumIdByTopic(topic);
				}
				
				topic.setPicasaId(albumEntry.getGphotoId());
				
				return topic;
			} else {
				AlbumEntry myAlbum = new AlbumEntry();
				myAlbum.setAccess(GphotoAccess.Value.PUBLIC);
				myAlbum.setTitle(new PlainTextConstruct(topic.getTopicName()));
				myAlbum.setDescription(new PlainTextConstruct(topic.getTopicDescription()));
				webClient.insertAlbum(myAlbum);
				// clear album
				albumMapById.clear();
				albumMapByTitle.clear();
				// recursive getAlbums
				return getAlbumIdByTopic(topic);
			}
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void invalidateWebClient() {
        webClient = null;
    }
	
	public void process() {
		if(isError) return;
		try {
			APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(settings.getApplicationType());
		    FileInputStream file = new FileInputStream(settings.getSyncDataFile());
		    XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    
		    List<Topics> topicCollections = new ArrayList<Topics>();
		    List<QuestionAnswers> questionAnswersCollections = new ArrayList<QuestionAnswers>();
		    
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    int index = 0;
		    while(rowIterator.hasNext()) {
		    	if(index++ == 0) {
		    		rowIterator.next();
		    		continue;
		    	}
		    	
		        Row row = rowIterator.next();
		        
		        // Extract Topic & QuestionAnswer
		        Topics topic = DotoQuizStructure.convertRowToTopics(row, type);
		        QuestionAnswers questionAnswer = DotoQuizStructure.convertRowToQuestions(row, type);
		        
		        if(topic != null) topicCollections.add(topic);
		        if(questionAnswer != null) questionAnswersCollections.add(questionAnswer);
		        
		        if(type == APPLICATION_TYPE.GENERATE_SQL) {
			        System.out.println(topic);
			        System.out.println(questionAnswer + "\n");
		        }
		    }
		    
		    if(type == APPLICATION_TYPE.BATCH_UPLOAD) {
		    	if(!topicCollections.isEmpty()) {
		    		System.out.println(getAlbumIdByTopic(topicCollections.get(0)));
		    	}
		    }
		    file.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new App(args).process();
	}
}