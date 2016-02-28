/*
	Copyright 2015 Denis Prasetio
	
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

package com.dotosoft.dotoquiz.tools;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dotosoft.dotoquiz.command.auth.impl.GoogleOAuth;
import com.dotosoft.dotoquiz.command.data.impl.GooglesheetClient;
import com.dotosoft.dotoquiz.tools.config.Settings;
import com.dotosoft.dotoquiz.tools.util.SyncState;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{
	private static final Logger log = LogManager.getLogger(OldApp.class.getName());
	
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
