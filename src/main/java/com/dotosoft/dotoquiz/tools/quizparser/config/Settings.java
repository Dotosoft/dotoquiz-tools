package com.dotosoft.dotoquiz.tools.quizparser.config;

import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import com.dotosoft.dotoquiz.config.Configuration;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.AchievementStructure;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.AnswerQuestionStructure;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.AuthenticationServer;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.ClientSecret;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.TopicStructure;

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
	
	private String dataStoreDir;
	private List<String> achievements;
	private List<String> datas;

	private ClientSecret clientSecret;
	private AuthenticationServer authenticationServer;

	private TopicStructure topicStructure;
	private AnswerQuestionStructure answerQuestionStructure;
	private AchievementStructure achievementStructure;

	public List<String> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<String> achievements) {
		this.achievements = achievements;
	}

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas) {
		this.datas = datas;
	}

	public String getDataStoreDir() {
		return dataStoreDir;
	}

	public void setDataStoreDir(String dataStoreDir) {
		this.dataStoreDir = dataStoreDir;
	}

	public ClientSecret getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(ClientSecret clientSecret) {
		this.clientSecret = clientSecret;
	}

	public AuthenticationServer getAuthenticationServer() {
		return authenticationServer;
	}

	public void setAuthenticationServer(
			AuthenticationServer authenticationServer) {
		this.authenticationServer = authenticationServer;
	}

	public AchievementStructure getAchievementStructure() {
		return achievementStructure;
	}

	public void setAchievementStructure(
			AchievementStructure achievementStructure) {
		this.achievementStructure = achievementStructure;
	}

	public TopicStructure getTopicStructure() {
		return topicStructure;
	}

	public void setTopicStructure(TopicStructure topicStructure) {
		this.topicStructure = topicStructure;
	}

	public AnswerQuestionStructure getAnswerQuestionStructure() {
		return answerQuestionStructure;
	}

	public void setAnswerQuestionStructure(
			AnswerQuestionStructure answerQuestionStructure) {
		this.answerQuestionStructure = answerQuestionStructure;
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
		log.error("Error: Could not run DataQuizParser.");
		log.info("Run: java -jar DataQuizParser.jar [file config] [CLEAR|DB|SYNC]");
	}

	public boolean loadSettings(String args[]) {
		try {
			if (args.length != 2) {
				showError();
				return false;
			}

			fileconfig = args[0];
			applicationType = args[1];

			InputStream in = Files.newInputStream(Paths.get(fileconfig));
			Settings setting = yaml.loadAs(in, Settings.class);
			BeanUtils.copyProperties(this, setting);
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
