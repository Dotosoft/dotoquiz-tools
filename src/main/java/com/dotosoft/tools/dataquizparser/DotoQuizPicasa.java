package com.dotosoft.tools.dataquizparser;

import java.io.File;

import org.apache.log4j.*;

import com.dotosoft.tools.dataquizparser.picasa.config.Settings;
import com.dotosoft.tools.dataquizparser.picasa.syncutil.SyncState;
import com.dotosoft.tools.dataquizparser.picasa.webclient.GoogleOAuth;
import com.dotosoft.tools.dataquizparser.picasa.webclient.PicasawebClient;
import com.dotosoft.tools.dataquizparser.picasa.webclient.RunRolledFileAppender;


public class DotoQuizPicasa {
	
	private static final Logger log = Logger.getLogger(DotoQuizPicasa.class);
	
	private Settings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	public DotoQuizPicasa() {
		initLogging();
		log.info("Starting and setup Doto Parser...");
		settings = new Settings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
	}
	
	public void startParse() {		
		if( settings.loadSettings() ) {
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
	
	private void initLogging() {

        ConsoleAppender console = new ConsoleAppender(); //create appender
        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
        console.setLayout(new PatternLayout(PATTERN));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        RunRolledFileAppender fa = new RunRolledFileAppender();
        fa.setName("FileLogger");
        fa.setFile(new File(System.getProperty("user.home"), "DotoParseSync.log").toString() );
        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fa.setThreshold(Level.INFO);
        fa.setAppend(true);
        fa.activateOptions();
        fa.setMaxBackupIndex( 1 );

        Logger.getRootLogger().addAppender(fa);
    }
	
	public void invalidateWebClient() {
        webClient = null;
    }
	
	public static void main(String args[]) {
		DotoQuizPicasa doto = new DotoQuizPicasa();
		doto.startParse();
	}
}
