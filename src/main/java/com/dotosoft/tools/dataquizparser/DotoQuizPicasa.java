package com.dotosoft.tools.dataquizparser;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dotosoft.tools.dataquizparser.picasa.config.Settings;
import com.dotosoft.tools.dataquizparser.picasa.syncutil.SyncState;
import com.dotosoft.tools.dataquizparser.picasa.webclient.GoogleOAuth;
import com.dotosoft.tools.dataquizparser.picasa.webclient.PicasawebClient;
import com.google.gdata.data.Person;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.util.ServiceException;


public class DotoQuizPicasa {
	
	private static final Logger log = LogManager.getLogger(DotoQuizPicasa.class.getName());
	
	private Settings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	public DotoQuizPicasa() {
		log.info("Starting and setup Doto Parser...");
		settings = new Settings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
		
		if( settings.loadSettings(new String[]{}) ) {
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	                webClient = auth.authenticatePicasa(settings, true, syncState );
	            }
	            catch( Exception _ex ) {
	                log.error( "Exception while authenticating.", _ex );
	                invalidateWebClient();
	            }
	
	            if( webClient != null )
	            {
	                log.info("Connection established.");
	            }
	            else{
	                log.warn("Unable to re-authenticate. User will need to auth interactively.");
	            }
	        }
		}
	}
	
	public void startParse() {		
		try {
			List<GphotoEntry> albumEntries = webClient.getAlbums(true);
			for(GphotoEntry album : albumEntries) {
				System.out.println("album::: " + album);
			} 
			
//			AlbumEntry myAlbum = new AlbumEntry();
//			myAlbum.setTitle(new PlainTextConstruct("Trip to France"));
//			myAlbum.setDescription(new PlainTextConstruct("My recent trip to France was delightful!"));
//			webClient.insertAlbum(myAlbum);
			
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void invalidateWebClient() {
        webClient = null;
    }
	
	public static void main(String args[]) {
		DotoQuizPicasa doto = new DotoQuizPicasa();
		doto.startParse();
	}
}
