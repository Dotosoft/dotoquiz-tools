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

package com.dotosoft.dotoquiz.tools.config;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.beanutils.BeanUtils;
import org.yaml.snakeyaml.Yaml;

import com.dotosoft.dotoquiz.config.Configuration;

public class Settings {

	private Yaml yaml = new Yaml();

	private Configuration configuration;
	private StructureConfig structure;
	private APIConfig api;
	
	private ImageConfig imageConfig;

	private String fileconfig;

	private String command;
	private String dataType;
	private String imageHostingType;

	private String syncDataFile;
	private String syncDataFolder;

	private boolean replaced;
	
	public ImageConfig getImageConfig() {
		return imageConfig;
	}

	public void setImageConfig(ImageConfig imageConfig) {
		this.imageConfig = imageConfig;
	}

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

	public void setCommand(String command) {
		this.command = command;
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
		return command;
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

	public boolean loadSettings(String args[]) {
		try {
			if (args.length != 2) {
				return false;
			}
			
			fileconfig = args[1];
			InputStream in = Files.newInputStream(Paths.get(fileconfig));
			Settings setting = yaml.loadAs(in, Settings.class);
			BeanUtils.copyProperties(this, setting);
			command = args[0];
		} catch (Exception ex) {
			ex.printStackTrace();
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
