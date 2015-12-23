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

package com.dotosoft.tools.dataquizparser.picasa.config;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * General settings class for loading/saving prefs
 */
public class Settings {
    private static final Logger log = Logger.getLogger(Settings.class);
    private Preferences preferences;

    private static final String REFRESH_TOKEN = "RefreshToken";
    private static final String SYNC_FOLDER = "SyncFolder";

    private File photoRootFolder;
    private String refreshToken;

    public String getRefreshToken() { return refreshToken; }
    public File getPhotoRootFolder() { return photoRootFolder; }

    public void setRefreshToken( String token ) { refreshToken = token; saveSettings(); }

    public Settings() {
        preferences = Preferences.userNodeForPackage(Settings.class);
    }

    public boolean loadSettings() {

        boolean result = true;
        photoRootFolder = null;

        String prefsFolder = preferences.get( SYNC_FOLDER, null );
        if( prefsFolder != null )
            photoRootFolder = new File( prefsFolder );

        if( photoRootFolder == null || ! photoRootFolder.exists() ) {
            result = setPhotoRootFolder();
        }

        refreshToken = preferences.get( REFRESH_TOKEN, null );
        
        log.info( "Settings loaded successfully.");
        return result;
    }

    public boolean setPhotoRootFolder() {

        // No folder, or the chosen folder isn't there. So prompt the user
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select Root Folder for Sync");

        if( chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION )
        {
            photoRootFolder = chooser.getSelectedFile();

            log.info( "Sync folder set to:" + photoRootFolder );
            saveSettings();
            return true;
        }

        return false;
    }

    public void saveSettings() {
        preferences.put( SYNC_FOLDER, getPhotoRootFolder().toString() );
        
        if( getRefreshToken() != null )
            preferences.put( REFRESH_TOKEN, getRefreshToken() );
        else
            preferences.remove( REFRESH_TOKEN );

        log.info( "Settings saved successfully.");
    }
}

