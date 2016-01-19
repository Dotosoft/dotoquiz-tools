package com.dotosoft.dotoquiz.tools.quizparser.config;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.dotosoft.dotoquiz.config.Configuration;

public class Settings {

	private static final Logger log = Logger.getLogger(Settings.class);
	private Yaml yaml = new Yaml();

	private Configuration configuration;
	private StructureConfig structure;
	private APIConfig api;

	private String fileconfig;

	private String applicationType;
	private String dataType;
	private String imageHostingType;

	private String syncDataFile;
	private String syncDataFolder;

	private boolean replaced;

	public String getFileconfig() {
		return fileconfig;
	}

	public void setFileconfig(String fileconfig) {
		this.fileconfig = fileconfig;
	}

	public boolean getReplaced() {
		return replaced;
	}

	public void setReplaced(boolean replaced) {
		this.replaced = replaced;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public StructureConfig getStructure() {
		return structure;
	}

	public void setStructure(StructureConfig structure) {
		this.structure = structure;
	}

	public APIConfig getApi() {
		return api;
	}

	public void setApi(APIConfig api) {
		this.api = api;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getApplicationType() {
		return applicationType;
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
		log.error("Error: Could not run DataQuizParser.");
		log.info("Run: java -jar DataQuizParser.jar [CLEAR|DB|SYNC] [file config]");
	}

	public boolean loadSettings(String args[]) {
		try {
			if (args.length != 2) {
				showError();
				return false;
			}
			
			fileconfig = args[1];

			InputStream in = Files.newInputStream(Paths.get(fileconfig));
			Settings setting = yaml.loadAs(in, Settings.class);
			BeanUtils.copyProperties(this, setting);
			
			applicationType = args[0].toUpperCase();
		} catch (Exception ex) {
			ex.printStackTrace();
			showError();
			return false;
		}

		return true;
	}

	public boolean saveSettings() {
		try {
			Writer writer = new FileWriter(fileconfig);
			yaml.dump(this, writer);
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}

		return true;
	}
}
