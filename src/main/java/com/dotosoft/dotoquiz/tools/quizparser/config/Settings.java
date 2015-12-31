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

package com.dotosoft.dotoquiz.tools.quizparser.config;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;

import com.dotosoft.dotoquiz.common.DotoQuizConstant;
import com.dotosoft.dotoquiz.common.QuizParserConstant;
import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.common.QuizParserConstant.DATA_TYPE;
import com.dotosoft.dotoquiz.common.QuizParserConstant.IMAGE_HOSTING_TYPE;

/**
 * General settings class for loading/saving prefs
 */
public class Settings {
    private static final Logger log = Logger.getLogger(Settings.class);
    
    private Preferences preferences;

    private String applicationType;
    private String dataType;
    private String imageHostingType;
    private String refreshToken;
    private File syncDataFile;

    public String getDataType() { return dataType; }
    public String getImageHostingType() { return imageHostingType; }
    public String getRefreshToken() { return refreshToken; }
    public String getApplicationType() { return applicationType; }
    public File getSyncDataFile() { return syncDataFile; }
    public String getSyncDataFolder() {
    	if(syncDataFile == null || "null".equals(syncDataFile.getPath())) {
    		return "Data";
    	}
    	return syncDataFile.getParent(); 
    }

    public void setRefreshToken( String token ) { refreshToken = token; saveSettings(); }

    public Settings() {
        preferences = Preferences.userNodeForPackage(Settings.class);
    }
    
    private void ClearPreferences() {
    	try {
			preferences.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
    }

    public boolean loadSettings(String args[]) {
    	
        applicationType = null;
        syncDataFile = null;
        refreshToken = preferences.get( QuizParserConstant.REFRESH_TOKEN, null );
        applicationType = preferences.get( QuizParserConstant.APP_TYPE, null );
        dataType = preferences.get( QuizParserConstant.DATA_TYPE, null );
        imageHostingType = preferences.get( QuizParserConstant.IMAGE_HOSTING_TYPE, null );
        
        String prefsFolder = preferences.get( QuizParserConstant.SYNC_FILE, null );
        if( prefsFolder != null ) {
        	syncDataFile = new File( prefsFolder );
        }
        
        if( args.length == 2 ) {
        	if(APPLICATION_TYPE.CLEAR.toString().equals(args[0])) {
				applicationType = args[0];
        	} else {
        		showError();
        	}
        	
        	if(IMAGE_HOSTING_TYPE.valueOf(args[1]) != null) {
				imageHostingType = args[1];
        	} else {
        		showError();
        	}
        	
        	saveSettings();
        }
        // if data type is excel, it must add 4 argument, need file excel
        // else if data type is googlesheet, it must add 3 argument
        else if(args.length >= 3 &&  args.length <= 4) {
        	try {
        		if(APPLICATION_TYPE.CLEAR.toString().equals(args[0])) {
        			showError();
        		}
        		
        		if(APPLICATION_TYPE.valueOf(args[0]) != null) {
					applicationType = args[0];
	        	} else {
	        		showError();
	        	}
        		
        		if(IMAGE_HOSTING_TYPE.valueOf(args[1]) != null) {
					imageHostingType = args[1];
	        	} else {
	        		showError();
	        	}
        		
	        	if(DATA_TYPE.valueOf(args[2]) != null) {
					dataType = args[2];
					
					if(DATA_TYPE.EXCEL.equals(dataType)) {
						syncDataFile = new File(args[3]);
					}
	        	} else {
	        		showError();
	        	}
	        	
				saveSettings();
				
        	} catch(IllegalArgumentException ex) {
        		showError();
        	}
        } else {
        	if( syncDataFile == null || ! syncDataFile.exists() || applicationType == null || dataType == null || imageHostingType == null ) {
				showError();
			}
        }
        
        log.info( "Data Type : " + dataType);
        log.info( "Image Hosting Type : " + imageHostingType);
        log.info( "Application Type : " + applicationType);
        log.info( "Sync Data File : '" + (syncDataFile == null ? "" : syncDataFile.getPath()) + "'");
        log.info( "Settings loaded successfully.");
        return true;
    }
    
    public void showError() {
    	log.error( "Error: Could not run DataQuizParser.");
    	log.info("Run: java -jar DataQuizParser.jar [CLEAR|DB|SYNC] [PICASA] [GOOGLESHEET|EXCEL] [File Excel]");
		System.exit(1);
    }

    public void saveSettings() {
        preferences.put( QuizParserConstant.SYNC_FILE, String.valueOf(getSyncDataFile()) );
        preferences.put( QuizParserConstant.APP_TYPE, getApplicationType() );
        preferences.put( QuizParserConstant.IMAGE_HOSTING_TYPE, getImageHostingType() );
        if(getDataType() != null) preferences.put( QuizParserConstant.DATA_TYPE, getDataType() );
        
        if( getRefreshToken() != null ) preferences.put( QuizParserConstant.REFRESH_TOKEN, getRefreshToken() );
        else preferences.remove( QuizParserConstant.REFRESH_TOKEN );

        log.info( "Settings saved successfully.");
    }
}

