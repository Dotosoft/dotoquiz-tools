package com.dotosoft.dotoquiz.tools.quizparser.config;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.dotosoft.dotoquiz.config.Configuration;

public class Settings extends Configuration {
	
	private static final Logger log = Logger.getLogger(Settings.class);
	private Yaml yaml = new Yaml();
	
	private String fileconfig;
	
	private String applicationType;
	private String dataType;
	private String imageHostingType;
	private String refreshToken;
	private String syncDataFile;
	private String syncDataFolder;

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getImageHostingType() {
		return imageHostingType;
	}

	public void setImageHostingType(String imageHostingType) {
		this.imageHostingType = imageHostingType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getSyncDataFile() {
		return syncDataFile;
	}

	public void setSyncDataFile(String syncDataFile) {
		this.syncDataFile = syncDataFile;
	}

	public String getSyncDataFolder() {
		return syncDataFolder;
	}

	public void setSyncDataFolder(String syncDataFolder) {
		this.syncDataFolder = syncDataFolder;
	}
	
	public void showError() {
    	log.error( "Error: Could not run DataQuizParser." );
    	log.info( "Run: java -jar DataQuizParser.jar [file config] [CLEAR|DB|SYNC]" );
    }

	public boolean loadSettings(String args[]) {
		try {
			fileconfig = args[ 0 ]; 
			
			InputStream in = Files.newInputStream( Paths.get( fileconfig ) );
	        Configuration config = yaml.loadAs( in, Configuration.class );
	        log.info( config );
	        Writer writer = new FileWriter( "output-" + args[ 0 ] );
	        yaml.dump(config, writer);
	        writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			showError();
			return false;
		}
		
		return true;
	}
	
	public boolean saveSettings() {
		try {
			Writer writer = new FileWriter( "output-" + fileconfig );
	        yaml.dump(this, writer);
	        writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
}
