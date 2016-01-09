package com.dotosoft.tools.dataquizparser;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dotosoft.dotoquiz.tools.quizparser.App;
import com.dotosoft.dotoquiz.tools.quizparser.auth.GoogleOAuth;
import com.dotosoft.dotoquiz.tools.quizparser.config.Settings;
import com.dotosoft.dotoquiz.tools.quizparser.data.GooglesheetClient;
import com.dotosoft.dotoquiz.tools.quizparser.utils.SyncState;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{
	private static final Logger log = LogManager.getLogger(App.class.getName());
	
	private Settings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private GooglesheetClient webClient;
	
	private boolean isError = false;
	
	private String[] args = new String[]{};
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws ServiceException 
     * @throws IOException 
     */
    public void testApp() throws IOException, ServiceException
    {
    	settings = new Settings();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			auth = new GoogleOAuth(settings);
			
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	                webClient = auth.authenticateGooglesheet("Pertanyaan", settings, false, syncState );
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
	            
	            WorksheetEntry worksheetEntry = webClient.getWorksheet(0);
	            webClient.getListRows(worksheetEntry);
	        }
		} else {
			isError = true;
			log.error( "Error: Could not run DataQuizParser.");
			System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
		}
		
        assertTrue( true );
    }
    
    private void invalidateWebClient() {
        webClient = null;
    }
}
