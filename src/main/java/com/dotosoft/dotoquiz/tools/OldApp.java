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

import static com.dotosoft.dotoquiz.tools.util.HibernateUtil.buildSessionFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dotosoft.dotoquiz.common.QuizConstant;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.DATA_TYPE;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.IMAGE_HOSTING_TYPE;
import com.dotosoft.dotoquiz.tools.config.Settings;
import com.dotosoft.dotoquiz.tools.thirdparty.GoogleOAuth;
import com.dotosoft.dotoquiz.tools.thirdparty.GooglesheetClient;
import com.dotosoft.dotoquiz.tools.thirdparty.PicasawebClient;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.DataQuestionsParser;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.DataTopicsParser;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.ParameterAchievementParser;
import com.dotosoft.dotoquiz.tools.util.DotoQuizStructure;
import com.dotosoft.dotoquiz.tools.util.HibernateUtil;
import com.dotosoft.dotoquiz.tools.util.SyncState;
import com.dotosoft.dotoquiz.utils.FileUtils;
import com.dotosoft.dotoquiz.utils.MD5Checksum;
import com.dotosoft.dotoquiz.utils.StringUtils;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.Lists;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class OldApp {
	
	private static final Logger log = LogManager.getLogger(OldApp.class.getName());
	
	private Settings settings;
	private Credential auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	private Map<String, Map<String, GphotoEntry>> photoMapByAlbumId;
	private Map<String, GphotoEntry> albumMapByTopicId;
	private Map<String, GphotoEntry> albumMapByTitle;
	private Map<String, DataTopicsParser> topicMapByTopicId;
	
	public static void main(String[] args) throws Exception {
		new OldApp(args).process();
	}
	
	public OldApp(String args[]) throws Exception {
		
		photoMapByAlbumId = new HashMap<String, Map<String, GphotoEntry>>();
		albumMapByTopicId = new HashMap<String, GphotoEntry>();
		albumMapByTitle = new HashMap<String, GphotoEntry>();
		topicMapByTopicId = new HashMap<String, DataTopicsParser>();
		
		settings = new Settings();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			log.info("Starting and setup Doto Parser...");
			
			auth = GoogleOAuth.authenticate(settings);
			
			if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
				log.info("Building hibernate...");
				buildSessionFactory(settings);
			}
			
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	            	webClient = new PicasawebClient(auth);
	            }
	            catch( Exception _ex ) {
	            	// settings.showError();
	                log.error( "Exception while authenticating.", _ex );
	                invalidateWebClient();
	            }
	
	            if( webClient != null )
	            {
	                log.info("Connection established.");
	            }
	            else{
	                log.warn("Unable to re-authenticate. User will need to auth interactively.");
	                // settings.showError();
	            }
	        }
		}
	}
	
	public void process() {
		if(webClient != null) {
			// Init Picasa Data
			if(IMAGE_HOSTING_TYPE.PICASA.toString().equals(settings.getImageHostingType())) {
				refreshDataFromPicasa();
				
				if(APPLICATION_TYPE.CLEAR.toString().equals(settings.getApplicationType())) {
					clearPicasa();
				} else {
					processPicasa();
				}
			}
		}
	}
	
	private void clearPicasa() {
		for(String key : albumMapByTitle.keySet()) {
			try {
				GphotoEntry albumEntry = albumMapByTitle.get(key);
				albumEntry.delete();
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void processPicasa() {
		if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
			log.info("process data from excel!");
		} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
			log.info("process data from googlesheet!");
		}
		
		// variable for googlesheet
		GooglesheetClient googlesheetClient = null;
		WorksheetEntry fullSheet = null;
		
		// variable for excel
		FileInputStream file = null;
		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		
		// variable for DB
		Session session = null;
		Transaction trx = null;

		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(settings.getApplicationType());
		
		try {
			if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
				session = HibernateUtil.getSessionFactory().openSession();
	    		trx = session.beginTransaction();
			}
			
			if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
			    file = new FileInputStream(settings.getSyncDataFile());
			    workbook = new XSSFWorkbook(file);			 
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				googlesheetClient = new GooglesheetClient(auth, settings.getSyncDataFile());
			}
			
			int index = 0;
			
			// --------------------------------------------------------------------------
			// Extract Achievement ------------------------------------------------------
			// --------------------------------------------------------------------------
			
			// parent folder
			if(!APPLICATION_TYPE.SHOW_COLUMN_HEADER.toString().equals(settings.getApplicationType())) {
				DataTopicsParser topicAchievement = new DataTopicsParser(QuizParserConstant.ACHIEVEMENT_NAME, QuizParserConstant.EMPTY_STRING, QuizParserConstant.EMPTY_STRING, QuizParserConstant.EMPTY_STRING, QuizParserConstant.ACHIEVEMENT_NAME, QuizParserConstant.ACHIEVEMENT_DESCRIPTION, QuizParserConstant.ACHIEVEMENT_IMAGE_URL, QuizConstant.NO, new java.util.Date(), QuizConstant.SYSTEM_USER, QuizConstant.NO, type);
				topicAchievement = syncTopicToPicasa(topicAchievement);
				topicMapByTopicId.put(topicAchievement.getId(), topicAchievement);
			}
			
			List listRow = null;
			for(String achievementTab : settings.getStructure().getTabAchievements().split(";")) {
				String sheetName = "";
				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {			 
				    sheet = workbook.getSheetAt(Integer.parseInt(achievementTab));
				    listRow = Lists.newArrayList(sheet.iterator());
				    sheetName = sheet.getSheetName();
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				    fullSheet = (WorksheetEntry) googlesheetClient.getWorksheet(Integer.parseInt(achievementTab));
				    listRow = googlesheetClient.getListRows(fullSheet);
				    sheetName = fullSheet.getTitle().getPlainText();
				}
				
			    for(Object row : listRow) {
			    	if(showColumnHeader(row, sheetName)) break;
			    	
			    	ParameterAchievementParser achievement = DotoQuizStructure.convertDataToAchievement(row, settings);
			    	
			        if(achievement != null) {
			        	if(type == APPLICATION_TYPE.DB) {
			        		session.saveOrUpdate( achievement.toParameterAchievements() );
			        		log.info("Save or update achievement: " + achievement);
			    		} else if(type == APPLICATION_TYPE.SYNC) {
			    			achievement = syncAchievementToPicasa(achievement);
			    			
			    			if(!QuizConstant.YES.equals(achievement.getIsProcessed())) {
			    				GooglesheetClient.updateSyncPicasa(settings, QuizParserConstant.PARSE_ACHIEVEMENT, row, achievement.getPicasaId(), achievement.getImagePicasaUrl(), QuizConstant.YES);
			    			}
			    		}
			        }
			    }
			}
			
			trx = CommitDB(trx, session, true);
			
			for(String dataTab : settings.getStructure().getTabTopics().split(";")) {
				// --------------------------------------------------------------------------
			    // Extract Topic
			    // --------------------------------------------------------------------------
				index = 0;
				String sheetName = "";
				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) { 
				    sheet = workbook.getSheetAt(Integer.parseInt(dataTab));
				    listRow = Lists.newArrayList(sheet.iterator());
				    sheetName = sheet.getSheetName();
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				    fullSheet = (WorksheetEntry) googlesheetClient.getWorksheet(Integer.parseInt(dataTab));
				    listRow = googlesheetClient.getListRows(fullSheet);
				    sheetName = fullSheet.getTitle().getPlainText();
				}
				
			    for(Object row : listRow) {
			    	if(showColumnHeader(row, sheetName)) break;
			    	
			    	DataTopicsParser topic = DotoQuizStructure.convertDataToTopics(row, settings);
			    	
			        if(topic != null) {
			        	if(type == APPLICATION_TYPE.DB) {
			        		if(StringUtils.hasValue(topic.getTopicParentId())) {
			        			topic.setDatTopics( topicMapByTopicId.get(topic.getTopicParentId()).toDataTopics() );
			        		}
			        		session.saveOrUpdate( topic.toDataTopics() );
			    			log.info("Save or update topic: " + topic);
			    		} else if(type == APPLICATION_TYPE.SYNC) {
			    			topic = syncTopicToPicasa(topic);
			    			
			    			if(!QuizConstant.YES.equals(topic.getIsProcessed())) {
			    				GooglesheetClient.updateSyncPicasa(settings, QuizParserConstant.PARSE_TOPIC, row, topic.getPicasaId(), topic.getImagePicasaUrl(), QuizConstant.YES);
			    			}
			    		}
			        	
			        	topicMapByTopicId.put(topic.getId(), topic);
			        }
			    }
			}
			
			trx = CommitDB(trx, session, true);
			    
			for(String dataTab : settings.getStructure().getTabQuestions().split(";")) {
			    // --------------------------------------------------------------------------
			    // Extract QuestionAnswers
			    // --------------------------------------------------------------------------
			    index = 0;
				String sheetName = "";
				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) { 
				    sheet = workbook.getSheetAt(Integer.parseInt(dataTab));
				    listRow = Lists.newArrayList(sheet.iterator());
				    sheetName = sheet.getSheetName();
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				    fullSheet = (WorksheetEntry) googlesheetClient.getWorksheet(Integer.parseInt(dataTab));
				    listRow = googlesheetClient.getListRows(fullSheet);
				    sheetName = fullSheet.getTitle().getPlainText();
				}
			    
			    for(Object row : listRow) {
			    	if(showColumnHeader(row, sheetName)) break;
			    	
			    	DataQuestionsParser questionAnswer = DotoQuizStructure.convertDataToAnswerQuestion(row, settings);
			    	
	    			if(questionAnswer != null) {
			        	if(type == APPLICATION_TYPE.DB) {
			        		questionAnswer.setMtQuestionType(HibernateUtil.getQuestionTypeByName(session, questionAnswer.getQuestionTypeData()));
			        		session.saveOrUpdate( questionAnswer.toDataQuestion() );
			        		log.info("Save or update QuestionAnswers: " + questionAnswer);
			        		for(String topicId : questionAnswer.getTopics()) {
			        			DataTopicsParser datTopic = topicMapByTopicId.get(topicId);
			        			HibernateUtil.saveOrUpdateTopicQuestionData(session, datTopic.toDataTopics(), questionAnswer.toDataQuestion());
			        		}
			    		} else if(type == APPLICATION_TYPE.SYNC) {
			    			questionAnswer = syncQuestionAnswersToPicasa(questionAnswer);		    	
			    			if(!QuizConstant.YES.equals(questionAnswer.getIsProcessed())) {
			    				GooglesheetClient.updateSyncPicasa(settings, QuizParserConstant.PARSE_QUESTION_ANSWER, row, questionAnswer.getPicasaId(), questionAnswer.getImagePicasaUrl(), QuizConstant.YES);
			    			}
			    		}
			        }
			    }
			}
			
			trx = CommitDB(trx, session, false);
		    
		    if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    	file.close();
			    log.info("Save data to file...");
			    FileOutputStream fos = new FileOutputStream(settings.getSyncDataFile());
			    workbook.write(fos);
			    fos.close();
		    }
		    
		    log.info("Done");
		} catch (Exception e) {
		    trx.rollback();
			session.close();
			
			e.printStackTrace();
		}
		
		System.exit(0);
	}
	
	private boolean showColumnHeader(Object data, String sheetName) {
		if(APPLICATION_TYPE.SHOW_COLUMN_HEADER.toString().equals(settings.getApplicationType())) {
			log.info("Show column header for \"" + sheetName + "\"");
			if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
				Row rowData = (Row) data;
				Iterator<Cell> cellIterator = rowData.iterator();
				int columnCount = rowData.getRowNum();
				for(int i=0;i<columnCount;i++) {
					Cell cell = rowData.getCell(i);
					log.info("\tColumn("+i+"): " + cell.getStringCellValue());
				}
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				ListEntry listEntry = (ListEntry) data;
				int index = 0;
				for (String tag : listEntry.getCustomElements().getTags()) {
					log.info("\tColumn" + (index++) + ": " + tag);
				}
			}
			return true;
		}
		
		return false;
	}
	
	private Transaction CommitDB(Transaction trx, Session session, boolean isReopened) {
		if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
	    	trx.commit();
	    	
	    	if(isReopened) {
	    		return session.beginTransaction();
	    	} else {
		    	session.close();
	    	}
		}
		
		return null;
	}
	
	private void refreshDataFromPicasa() {
		log.info("Load all data from picasa");
		try {
			List<GphotoEntry> albumEntries = webClient.getAlbums(true);
			for(GphotoEntry albumEntry : albumEntries) {
				// System.out.println("album::: " + album);
				albumMapByTitle.put(albumEntry.getTitle().getPlainText(), albumEntry);
				// Sync picture topic.png
				List<GphotoEntry> photoEntries = webClient.getPhotos(albumEntry);
				Map<String, GphotoEntry> photoEntriesCollections;
				if(photoMapByAlbumId.containsKey(albumEntry.getId())) {
					photoEntriesCollections = (Map<String, GphotoEntry>) photoMapByAlbumId.get(albumEntry.getId());  
				} else {
					photoEntriesCollections = new HashMap<String, GphotoEntry>();
				}
				for(GphotoEntry photoEntry : photoEntries) {
					photoEntriesCollections.put(photoEntry.getTitle().getPlainText(), photoEntry);
				}
				photoMapByAlbumId.put(albumEntry.getId(), photoEntriesCollections);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void invalidateWebClient() {
        webClient = null;
    }
	
	public ParameterAchievementParser syncAchievementToPicasa(ParameterAchievementParser achievement) {
		log.info("Sync achievement '" + achievement.getId() + "'");
		
		if(!QuizConstant.YES.equals(achievement.getIsProcessed())) {
			try {
				GphotoEntry achievementTopic = albumMapByTopicId.get(QuizParserConstant.ACHIEVEMENT_NAME);
				
				// Check image file is valid or not
				java.nio.file.Path achievementImagePath = FileUtils.getPath(settings.getSyncDataFolder(), achievementTopic.getTitle().getPlainText(), achievement.getImageUrl());
				if(!achievementImagePath.toFile().exists()) {
					log.error("File is not found at '" + achievementImagePath.toString() + "'. Please put the file and start this app again.");
					System.exit(1);
				}
				
				// Check image at picasa
				Map<String, GphotoEntry> photoEntryCollections = photoMapByAlbumId.get(achievementTopic.getId());
				GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get(achievement.getImageUrl()) : null;
				if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
				
				photoEntry = (GphotoEntry) webClient.uploadImageToAlbum(achievementImagePath.toFile(), photoEntry, achievementTopic, MD5Checksum.getMD5Checksum(achievementImagePath.toString()));
				photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
				photoMapByAlbumId.put(achievementTopic.getId(), photoEntryCollections);
				
				achievement.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
				achievement.setPicasaId( photoEntry.getGphotoId() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if(StringUtils.hasValue(achievement.getImagePicasaUrl())) {
				FileUtils.downloadFileToLocal(achievement.getImagePicasaUrl(), Paths.get(settings.getSyncDataFolder(), QuizParserConstant.ACHIEVEMENT_NAME, achievement.getImageUrl()).toString(), settings.getReplaced());
			}
		}
		
		return achievement;
	}
	
	public DataTopicsParser syncTopicToPicasa(DataTopicsParser topic) {
		log.info("Sync Topics '" + topic.getId() + "'");
		try {
			GphotoEntry albumEntry;
			if(albumMapByTitle.containsKey(topic.getId())) {
				albumEntry = albumMapByTitle.get(topic.getId());
			} else {
				// Upload photo as QuestionAnswer
				AlbumEntry myAlbum = new AlbumEntry();
				myAlbum.setAccess(GphotoAccess.Value.PUBLIC);
				myAlbum.setTitle(new PlainTextConstruct(topic.getId()));
				myAlbum.setDescription(new PlainTextConstruct(topic.getDescription()));
				albumEntry = (GphotoEntry) webClient.insertAlbum(myAlbum);
			}
			
			if(!QuizConstant.YES.equals(topic.getIsProcessed())) {
				Map<String, GphotoEntry> photoEntryCollections = (Map<String, GphotoEntry>) photoMapByAlbumId.get(albumEntry.getId());
				GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get(topic.getImageUrl()) : null;
				if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
				
				// Upload album as topic
				// log.info("there is no image '"+ topic.getImageUrl() +"' at '" + topic.getId() + "'. Wait for uploading...");
				java.nio.file.Path topicImagePath = FileUtils.getPath(settings.getSyncDataFolder(), topic.getId(), topic.getImageUrl());
				if(topicImagePath.getParent().toFile().exists()) {
					if(!topicImagePath.toFile().exists()) {
						log.error("File is not found at '" + topicImagePath.toString() + "'. Please put the file and start this app again.");
						System.exit(1);
					}
					photoEntry = (GphotoEntry) webClient.uploadImageToAlbum(topicImagePath.toFile(), photoEntry, albumEntry, MD5Checksum.getMD5Checksum(topicImagePath.toString()));
					photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
					photoMapByAlbumId.put(albumEntry.getId(), photoEntryCollections);
					
					topic.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
					topic.setPicasaId(albumEntry.getGphotoId());
				}
				
			} else {
				if(StringUtils.hasValue(topic.getImagePicasaUrl())) {
					FileUtils.downloadFileToLocal(topic.getImagePicasaUrl(), Paths.get(settings.getSyncDataFolder(), topic.getId(), topic.getImageUrl()).toString(), settings.getReplaced());
				}
			}
			albumMapByTopicId.put(topic.getId(), albumEntry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topic;
	}
	
	public DataQuestionsParser syncQuestionAnswersToPicasa(DataQuestionsParser answer) {
		log.info("Sync QuestionAnswers '" + answer.getId() + "'");
		
		if(!QuizConstant.YES.equals(answer.getIsProcessed())) {
			try {
				if("image".equalsIgnoreCase(answer.getQuestionTypeData()) && StringUtils.hasValue(answer.getAdditionalData())) {
					GphotoEntry firstTopic = albumMapByTopicId.get(answer.getTopics()[0]);
					// Check Topic is valid or not
					if(firstTopic == null) {
						log.error("Topic '"+ answer.getTopics()[0] +"' is not found!");
						System.exit(1);
					}
					
					// Check image file is valid or not
					java.nio.file.Path questionAnswerImagePath = FileUtils.getPath(settings.getSyncDataFolder(), firstTopic.getTitle().getPlainText(), answer.getAdditionalData());
					if(!questionAnswerImagePath.toFile().exists()) {
						log.error("File is not found at '" + questionAnswerImagePath.toString() + "'. Please put the file and start this app again.");
						System.exit(1);
					}
					
					// Check image at picasa
					Map<String, GphotoEntry> photoEntryCollections = photoMapByAlbumId.get(firstTopic.getId());
					GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get(answer.getAdditionalData()) : null;
					if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
					
					photoEntry = (GphotoEntry) webClient.uploadImageToAlbum(questionAnswerImagePath.toFile(), photoEntry, firstTopic, MD5Checksum.getMD5Checksum(questionAnswerImagePath.toString()));
					photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
					photoMapByAlbumId.put(firstTopic.getId(), photoEntryCollections);
					
					answer.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
					answer.setPicasaId( photoEntry.getGphotoId() );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if("image".equalsIgnoreCase(answer.getQuestionTypeData()) && StringUtils.hasValue(answer.getAdditionalData())) {
				GphotoEntry firstTopic = albumMapByTopicId.get(answer.getTopics()[0]);
				FileUtils.downloadFileToLocal(answer.getImagePicasaUrl(), Paths.get(settings.getSyncDataFolder(), firstTopic.getTitle().getPlainText(), answer.getAdditionalData()).toString(), settings.getReplaced());
			}
		}
		
		return answer;
	}
}