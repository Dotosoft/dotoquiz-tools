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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dotosoft.dotoquiz.common.DotoQuizConstant;
import com.dotosoft.dotoquiz.tools.quizparser.auth.GoogleOAuth;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant.DATA_TYPE;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant.IMAGE_HOSTING_TYPE;
import com.dotosoft.dotoquiz.tools.quizparser.config.OldSettings;
import com.dotosoft.dotoquiz.tools.quizparser.data.GooglesheetClient;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.DataQuestionsParser;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.DataTopicsParser;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.ParameterAchievementParser;
import com.dotosoft.dotoquiz.tools.quizparser.helper.DotoQuizStructure;
import com.dotosoft.dotoquiz.tools.quizparser.images.PicasawebClient;
import com.dotosoft.dotoquiz.tools.quizparser.utils.HibernateUtil;
import com.dotosoft.dotoquiz.tools.quizparser.utils.SyncState;
import com.dotosoft.dotoquiz.utils.FileUtils;
import com.dotosoft.dotoquiz.utils.MD5Checksum;
import com.dotosoft.dotoquiz.utils.StringUtils;
import com.google.api.client.util.Lists;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class App {
	
	private static final Logger log = LogManager.getLogger(App.class.getName());
	
	private OldSettings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	private Map<String, Map<String, GphotoEntry>> photoMapByAlbumId;
	private Map<String, GphotoEntry> albumMapByTopicId;
	private Map<String, GphotoEntry> albumMapByTitle;
	private Map<String, DataTopicsParser> topicMapByTopicId;
	
	public static void main(String[] args) {
		new App(args).process();
	}
	
	public App(String args[]) {
		log.info("Starting and setup Doto Parser...");
		
		photoMapByAlbumId = new HashMap<String, Map<String, GphotoEntry>>();
		albumMapByTopicId = new HashMap<String, GphotoEntry>();
		albumMapByTitle = new HashMap<String, GphotoEntry>();
		topicMapByTopicId = new HashMap<String, DataTopicsParser>();
		
		settings = new OldSettings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	                webClient = auth.authenticatePicasa(settings, false, syncState );
	            }
	            catch( Exception _ex ) {
	            	settings.showError();
	                log.error( "Exception while authenticating.", _ex );
	                invalidateWebClient();
	            }
	
	            if( webClient != null )
	            {
	                log.info("Connection established.");
	            }
	            else{
	                log.warn("Unable to re-authenticate. User will need to auth interactively.");
	                settings.showError();
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
				} else if(APPLICATION_TYPE.SYNC.toString().equals(settings.getApplicationType())
						|| APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
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
			
			List rowAchievements = null;
			if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
			    file = new FileInputStream(settings.getSyncDataFile());
			    workbook = new XSSFWorkbook(file);			 
			    sheet = workbook.getSheetAt(1);
			    rowAchievements = Lists.newArrayList(sheet.iterator());
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
				googlesheetClient = auth.authenticateGooglesheet("TopQuizData", settings, false, syncState );
			    fullSheet = googlesheetClient.getWorksheet(1);
			    rowAchievements = googlesheetClient.getListRows(fullSheet);
			}
			
			// Extract Achievement
			DataTopicsParser topicAchievement = new DataTopicsParser("-1", "", "", "", "achievement", "achievementDescription", "topic.png", DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, DotoQuizConstant.NO, type);
			topicAchievement = syncTopicToPicasa(topicAchievement);
			topicMapByTopicId.put(topicAchievement.getId(), topicAchievement);
			
		    int index = 0;
		    for(Object row : rowAchievements) {
		    	ParameterAchievementParser achievement = null;
		    	if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    		achievement = DotoQuizStructure.convertRowExcelToAchievement((Row) row, type);
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
					achievement = DotoQuizStructure.convertRowGooglesheetExcelToAchievement((ListEntry) row, type);
				}
		    	
		        if(achievement != null) {
		        	if(type == APPLICATION_TYPE.DB) {
		        		session.saveOrUpdate( achievement.toParameterAchievements() );
		        		log.info("Save or update achievement: " + achievement);
		    		} else if(type == APPLICATION_TYPE.SYNC) {
		    			achievement = syncAchievementToPicasa(achievement);
		    			
		    			if(!DotoQuizConstant.YES.equals(achievement.getIsProcessed())) {
		    				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    					Row rowData = (Row) row;
		    					rowData.getCell(1).setCellValue(achievement.getPicasaId());
		    					rowData.getCell(2).setCellValue(achievement.getImagePicasaUrl());
		    					rowData.getCell(7).setCellValue(DotoQuizConstant.YES);
		    				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
		    					ListEntry listEntry = (ListEntry) row;
		    					listEntry.getCustomElements().setValueLocal("albumidpicasa", achievement.getPicasaId());
				    			listEntry.getCustomElements().setValueLocal("imageurlpicasa", achievement.getImagePicasaUrl());
				    			listEntry.getCustomElements().setValueLocal("isprocessed", DotoQuizConstant.YES);
				    			listEntry.update();
		    				}
		    			}
		    		}
		        }
		    }
		    
		    if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
		    	trx.commit();
		    	trx = session.beginTransaction();
			}
			
		    // Extract Topic
		    List rows = null;
			if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) { 
			    sheet = workbook.getSheetAt(0);
			    rows = Lists.newArrayList(sheet.iterator());
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
			    fullSheet = googlesheetClient.getWorksheet(0);
			    rows = googlesheetClient.getListRows(fullSheet);
			}
		    index = 0;
		    for(Object row : rows) {
		    	DataTopicsParser topic = null;
		    	if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    		topic = DotoQuizStructure.convertRowExcelToTopics((Row) row, type);
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
					topic = DotoQuizStructure.convertRowGooglesheetExcelToTopics((ListEntry) row, type);
				}
		    	
		        if(topic != null) {
		        	if(type == APPLICATION_TYPE.DB) {
		        		if(StringUtils.hasValue(topic.getTopicParentId())) {
		        			topic.setDatTopics( topicMapByTopicId.get(topic.getTopicParentId()).toDataTopics() );
		        		}
		        		session.saveOrUpdate( topic.toDataTopics() );
		    			log.info("Save or update topic: " + topic);
		    		} else if(type == APPLICATION_TYPE.SYNC) {
		    			topic = syncTopicToPicasa(topic);
		    			
		    			if(!DotoQuizConstant.YES.equals(topic.getIsProcessed())) {
		    				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    					Row rowData = (Row) row;
		    					rowData.getCell(1).setCellValue(topic.getPicasaId());
		    					rowData.getCell(2).setCellValue(topic.getImagePicasaUrl());
		    					rowData.getCell(7).setCellValue(DotoQuizConstant.YES);
		    				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
		    					ListEntry listEntry = (ListEntry) row;
		    					listEntry.getCustomElements().setValueLocal("albumidpicasa", topic.getPicasaId());
				    			listEntry.getCustomElements().setValueLocal("imageurlpicasa", topic.getImagePicasaUrl());
				    			listEntry.getCustomElements().setValueLocal("isprocessed", DotoQuizConstant.YES);
				    			listEntry.update();
		    				}
		    			}
		    		}
		        	
		        	topicMapByTopicId.put(topic.getId(), topic);
		        }
		    }
		    
		    if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
		    	trx.commit();
		    	trx = session.beginTransaction();
			}
		    
		    // Extract QuestionAnswers
		    if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {			 
			    rows = Lists.newArrayList(sheet.iterator());
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
			    rows = googlesheetClient.getListRows(fullSheet);
			}
		    
		    index = 0;
		    for(Object row : rows) {
		    	DataQuestionsParser questionAnswer = null;
		    	
		    	if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    		questionAnswer = DotoQuizStructure.convertRowExcelToQuestions((Row) row, type);
				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
					questionAnswer = DotoQuizStructure.convertRowGooglesheetToQuestions((ListEntry) row, type);
				}
		    	
    			if(questionAnswer != null) {
		        	if(type == APPLICATION_TYPE.DB) {
		        		questionAnswer.setMtQuestionType(HibernateUtil.getQuestionTypeByName(session, questionAnswer.getQuestionTypeData()));
		        		session.saveOrUpdate( questionAnswer.toDataQuestion() );
		        		log.info("Save or update QuestionAnswers: " + questionAnswer);
		        		for(String topicId : questionAnswer.getTopics()) {
		        			DataTopicsParser datTopic = topicMapByTopicId.get(topicId);
		        			HibernateUtil.SaveOrUpdateTopicQuestionData(session, datTopic.toDataTopics(), questionAnswer.toDataQuestion());
		        		}
		    		} else if(type == APPLICATION_TYPE.SYNC) {
		    			questionAnswer = syncQuestionAnswersToPicasa(questionAnswer);		    	
		    			if(!DotoQuizConstant.YES.equals(questionAnswer.getIsProcessed())) {
		    				if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    					Row rowData = (Row) row;
				    			if("image".equalsIgnoreCase(questionAnswer.getQuestionTypeData())) {
				    				rowData.getCell(11).setCellValue(questionAnswer.getPicasaId());
				    				rowData.getCell(12).setCellValue(questionAnswer.getImagePicasaUrl());
				    			}
				    			rowData.getCell(20).setCellValue(DotoQuizConstant.YES);
		    				} else if(DATA_TYPE.GOOGLESHEET.toString().equals(settings.getDataType())) {
		    					ListEntry listEntry = (ListEntry) row;
		    					if("image".equalsIgnoreCase(questionAnswer.getQuestionTypeData())) {
					    			listEntry.getCustomElements().setValueLocal("photoidpicasa", questionAnswer.getPicasaId());
					    			listEntry.getCustomElements().setValueLocal("imageurlpicasa_2", questionAnswer.getImagePicasaUrl());
				    			}
				    			listEntry.getCustomElements().setValueLocal("isprocessed_2", DotoQuizConstant.YES);
				    			listEntry.update();
		    				}
		    			}
		    		}
		        }
		    }
		    
		    if(DATA_TYPE.EXCEL.toString().equals(settings.getDataType())) {
		    	file.close();
		    	
			    log.info("Save data to file...");
			    FileOutputStream fos = new FileOutputStream(settings.getSyncDataFile());
			    workbook.write(fos);
			    fos.close();
		    }
		    
		    
		    if(APPLICATION_TYPE.DB.toString().equals(settings.getApplicationType())) {
		    	trx.commit();
				session.close();
			}
		    
		    log.info("Done");
		} catch (Exception e) {
		    trx.rollback();
			session.close();
			
			e.printStackTrace();
		}
		
		System.exit(0);
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
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void invalidateWebClient() {
        webClient = null;
    }
	
	public ParameterAchievementParser syncAchievementToPicasa(ParameterAchievementParser achievement) {
		log.info("Sync achievement '" + achievement.getId() + "'");
		
		if(!DotoQuizConstant.YES.equals(achievement.getIsProcessed())) {
			try {
				GphotoEntry achievementTopic = albumMapByTopicId.get("-1");
				
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
				if(photoEntry == null) {
					photoEntry = webClient.uploadImageToAlbum(achievementImagePath.toFile(), null, achievementTopic, MD5Checksum.getMD5Checksum(achievementImagePath.toString()));
					photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
					photoMapByAlbumId.put(achievementTopic.getId(), photoEntryCollections);
				}
				
				achievement.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
				achievement.setPicasaId( photoEntry.getGphotoId() );
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
		}
		
		return achievement;
	}
	
	public DataTopicsParser syncTopicToPicasa(DataTopicsParser topic) {
		log.info("Sync Topics '" + topic.getId() + "'");
		try {
			GphotoEntry albumEntry;
			if(albumMapByTitle.containsKey(topic.getName())) {
				albumEntry = albumMapByTitle.get(topic.getName());
			} else {
				// Upload photo as QuestionAnswer
				AlbumEntry myAlbum = new AlbumEntry();
				myAlbum.setAccess(GphotoAccess.Value.PUBLIC);
				myAlbum.setTitle(new PlainTextConstruct(topic.getName()));
				myAlbum.setDescription(new PlainTextConstruct(topic.getDescription()));
				albumEntry = webClient.insertAlbum(myAlbum);
			}
			
			if(!DotoQuizConstant.YES.equals(topic.getIsProcessed())) {
				Map<String, GphotoEntry> photoEntryCollections = (Map<String, GphotoEntry>) photoMapByAlbumId.get(albumEntry.getId());
				GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get(topic.getImageUrl()) : null;
				if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
				if(photoEntry == null) {
					// Upload album as topic
					log.info("there is no image '"+ topic.getImageUrl() +"' at '" + topic.getName() + "'. Wait for uploading...");
					java.nio.file.Path topicImagePath = FileUtils.getPath(settings.getSyncDataFolder(), topic.getName(), topic.getImageUrl());
					if(!topicImagePath.toFile().exists()) {
						log.error("File is not found at '" + topicImagePath.toString() + "'. Please put the file and start this app again.");
						System.exit(1);
					}
					photoEntry = webClient.uploadImageToAlbum(topicImagePath.toFile(), null, albumEntry, MD5Checksum.getMD5Checksum(topicImagePath.toString()));
					photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
					photoMapByAlbumId.put(albumEntry.getId(), photoEntryCollections);
				}
				
				topic.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
				topic.setPicasaId(albumEntry.getGphotoId());
			}
			albumMapByTopicId.put(topic.getId(), albumEntry);
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
		return topic;
	}
	
	public DataQuestionsParser syncQuestionAnswersToPicasa(DataQuestionsParser answer) {
		log.info("Sync QuestionAnswers '" + answer.getId() + "'");
		
		if(!DotoQuizConstant.YES.equals(answer.getIsProcessed())) {
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
					if(photoEntry == null) {
						photoEntry = webClient.uploadImageToAlbum(questionAnswerImagePath.toFile(), null, firstTopic, MD5Checksum.getMD5Checksum(questionAnswerImagePath.toString()));
						photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
						photoMapByAlbumId.put(firstTopic.getId(), photoEntryCollections);
					}
					
					answer.setImagePicasaUrl( ((MediaContent)photoEntry.getContent()).getUri() );
					answer.setPicasaId( photoEntry.getGphotoId() );
				}
			} catch (IOException | ServiceException e) {
				e.printStackTrace();
			}
		}
		
		return answer;
	}
}