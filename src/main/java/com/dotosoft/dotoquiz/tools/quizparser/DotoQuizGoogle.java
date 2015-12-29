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

package com.dotosoft.dotoquiz.tools.quizparser;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dotosoft.dotoquiz.tools.quizparser.auth.GoogleOAuth;
import com.dotosoft.dotoquiz.tools.quizparser.config.Settings;
import com.dotosoft.dotoquiz.tools.quizparser.data.GooglesheetClient;
import com.dotosoft.dotoquiz.tools.quizparser.utils.SyncState;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class DotoQuizGoogle {
	
private static final Logger log = LogManager.getLogger(App.class.getName());
	
	private static Settings settings;
	private static GoogleOAuth auth;
	private static SyncState syncState;
	private static GooglesheetClient webClient;
	
	private static boolean isError = false;
	
	public static void main(String[] args) throws IOException, ServiceException, GeneralSecurityException {
		
		settings = new Settings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			
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
	}
	
	private static void invalidateWebClient() {
        webClient = null;
    }
}
