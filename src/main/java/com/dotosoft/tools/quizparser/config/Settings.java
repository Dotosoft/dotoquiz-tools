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

package com.dotosoft.tools.quizparser.config;

import org.apache.log4j.Logger;

import com.dotosoft.tools.quizparser.App.APPLICATION_TYPE;

import javax.swing.*;

import java.io.File;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * General settings class for loading/saving prefs
 */
public class Settings {
    private static final Logger log = Logger.getLogger(Settings.class);
    private Preferences preferences;

    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String APP_TYPE = "ApplicationType";
    private static final String SYNC_FILE = "SyncFile";

    private String applicationType;
    private File syncDataFile;
    private String refreshToken;

    public String getRefreshToken() { return refreshToken; }
    public String getApplicationType() { return applicationType; }
    public File getSyncDataFile() { return syncDataFile; }
    public String getSyncDataFolder() { return syncDataFile.getParent(); }

    public void setRefreshToken( String token ) { refreshToken = token; saveSettings(); }

    public Settings() {
        preferences = Preferences.userNodeForPackage(Settings.class);
    }

    public boolean loadSettings(String args[]) {

        applicationType = null;
        syncDataFile = null;
        refreshToken = preferences.get( REFRESH_TOKEN, null );
        applicationType = preferences.get( APP_TYPE, null );
        String prefsFolder = preferences.get( SYNC_FILE, null );
        if( prefsFolder != null ) {
        	syncDataFile = new File( prefsFolder );
        }

        if(args.length == 2) {
        	try {
	        	if(APPLICATION_TYPE.valueOf(args[0]) != null) {
					applicationType = args[0];
					syncDataFile = new File(args[1]);
					saveSettings();
	        	}
        	} catch(IllegalArgumentException ex) {
        		System.out.println("Error: Could not run DataQuizParser.");
				System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
				System.exit(1);
        	}
        } else {
        	if( syncDataFile == null || ! syncDataFile.exists() || applicationType == null) {
				System.out.println("Error: Could not run DataQuizParser.");
				System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
				System.exit(1);
			}
        }
        
        log.info( "Application Type : " + applicationType);
        log.info( "Sync Data File : " + syncDataFile.getPath());
        log.info( "Settings loaded successfully.");
        return true;
    }

    public void saveSettings() {
        preferences.put( SYNC_FILE, getSyncDataFile().toString() );
        preferences.put( APP_TYPE, getApplicationType() );
        
        if( getRefreshToken() != null ) preferences.put( REFRESH_TOKEN, getRefreshToken() );
        else preferences.remove( REFRESH_TOKEN );

        log.info( "Settings saved successfully.");
    }
}

