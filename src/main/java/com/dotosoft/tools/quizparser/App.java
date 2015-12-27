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

package com.dotosoft.tools.quizparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.dotosoft.tools.quizparser.auth.GoogleOAuth;
import com.dotosoft.tools.quizparser.config.Settings;
import com.dotosoft.tools.quizparser.helper.DotoQuizStructure;
import com.dotosoft.tools.quizparser.helper.FileUtils;
import com.dotosoft.tools.quizparser.helper.MD5Checksum;
import com.dotosoft.tools.quizparser.helper.StringUtils;
import com.dotosoft.tools.quizparser.images.PicasawebClient;
import com.dotosoft.tools.quizparser.images.syncutil.SyncState;
import com.dotosoft.tools.quizparser.representations.QuestionAnswers;
import com.dotosoft.tools.quizparser.representations.Topics;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.GphotoAccess;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.util.ServiceException;

public class App {
	
	private static final Logger log = LogManager.getLogger(App.class.getName());
	
	private Settings settings;
	private GoogleOAuth auth;
	private SyncState syncState;
	private PicasawebClient webClient;
	
	private Map<String, Map<String, GphotoEntry>> photoMapByAlbumId;
	private Map<String, GphotoEntry> albumMapByTopicId;
	private Map<String, GphotoEntry> albumMapByTitle;
	
	private boolean isError = false;
	
	public enum APPLICATION_TYPE {
		GENERATE_SQL, BATCH_UPLOAD, ALL
	}
	
	public App(String args[]) {
		log.info("Starting and setup Doto Parser...");
		photoMapByAlbumId = new HashMap<String, Map<String, GphotoEntry>>();
		albumMapByTopicId = new HashMap<String, GphotoEntry>();
		albumMapByTitle = new HashMap<String, GphotoEntry>();
		
		settings = new Settings();
		auth = new GoogleOAuth();
		syncState = new SyncState();
		
		if( settings.loadSettings(args) ) {
			
			log.info("Initialising Web client and authenticating...");
	        if( webClient == null ) {
	            try {
	                webClient = auth.authenticatePicasa(settings, false, syncState );
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
	        }
		} else {
			isError = true;
			log.error( "Error: Could not run DataQuizParser.");
			System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
		}
	}
	
	private void refreshAllData() {
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
	
	public void process() {
		if(isError) return;
		try {
			APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(settings.getApplicationType());
		    FileInputStream file = new FileInputStream(settings.getSyncDataFile());
		    XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    List<Topics> topicCollections = new ArrayList<Topics>();
		    List<QuestionAnswers> questionAnswersCollections = new ArrayList<QuestionAnswers>();
		    
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    int index = 0;
		    while(rowIterator.hasNext()) {
		    	if(index++ == 0) {
		    		rowIterator.next();
		    		continue;
		    	}
		        Row row = rowIterator.next();
		        // Extract Topic & QuestionAnswer
		        Topics topic = DotoQuizStructure.convertRowToTopics(row, type);
		        QuestionAnswers questionAnswer = DotoQuizStructure.convertRowToQuestions(row, type);
		        if(topic != null) topicCollections.add(topic);
		        if(questionAnswer != null) questionAnswersCollections.add(questionAnswer);
		    }
		    // Init Picasa Data
		    refreshAllData();
		    // Sync Topic
	    	for(Topics topic : topicCollections) {
	    		if(type == APPLICATION_TYPE.GENERATE_SQL) {
	    			System.out.println(topic);
	    		} else if(type == APPLICATION_TYPE.BATCH_UPLOAD || type == APPLICATION_TYPE.ALL) {
	    			System.out.println(uploadTopicToPicasa(topic));
	    		}
	    	}
	    	// Sync Album
	    	for(QuestionAnswers questionAnswer : questionAnswersCollections) {
	    		if(type == APPLICATION_TYPE.GENERATE_SQL) {
	    			System.out.println(questionAnswer + "\n");
	    		} else if(type == APPLICATION_TYPE.BATCH_UPLOAD || type == APPLICATION_TYPE.ALL) {
	    			System.out.println(uploadQuestionAnswersToPicasa(questionAnswer));		    			
	    		}
	    	}
		    file.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public Topics uploadTopicToPicasa(Topics topic) {
		log.info("Upload Topics '" + topic.getTopicName() + "'");
		try {
			GphotoEntry albumEntry;
			if(albumMapByTitle.containsKey(topic.getTopicName())) {
				albumEntry = albumMapByTitle.get(topic.getTopicName());
			} else {
				// Upload photo as QuestionAnswer
				AlbumEntry myAlbum = new AlbumEntry();
				myAlbum.setAccess(GphotoAccess.Value.PUBLIC);
				myAlbum.setTitle(new PlainTextConstruct(topic.getTopicName()));
				myAlbum.setDescription(new PlainTextConstruct(topic.getTopicDescription()));
				albumEntry = webClient.insertAlbum(myAlbum);
			}
			
			Map<String, GphotoEntry> photoEntryCollections = (Map<String, GphotoEntry>) photoMapByAlbumId.get(albumEntry.getId());
			GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get("topic.png") : null;
			if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
			if(photoEntry == null) {
				// Upload album as topic
				log.info("there is no image 'topic' at '" + topic.getTopicName() + "'. Wait for uploading...");
				java.nio.file.Path topicImagePath = FileUtils.getPath(settings.getSyncDataFolder(), topic.getTopicName(), "topic.png");
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
			albumMapByTopicId.put(topic.getId(), albumEntry);
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
		return topic;
	}
	
	public QuestionAnswers uploadQuestionAnswersToPicasa(QuestionAnswers answer) {
		log.info("Upload QuestionAnswers '" + answer.getPertanyaan() + "'");
		
		try {
			if(StringUtils.hasValue(answer.getImageUrl())) {
				GphotoEntry firstTopic = albumMapByTopicId.get(answer.getTopics()[0]);
				// Check Topic is valid or not
				if(firstTopic == null) {
					log.error("Topic '"+ answer.getTopics()[0] +"' is not found!");
					System.exit(1);
				}
				
				// Check image file is valid or not
				java.nio.file.Path questionAnswerImagePath = FileUtils.getPath(settings.getSyncDataFolder(), firstTopic.getTitle().getPlainText(), answer.getImageUrl());
				if(!questionAnswerImagePath.toFile().exists()) {
					log.error("File is not found at '" + questionAnswerImagePath.toString() + "'. Please put the file and start this app again.");
					System.exit(1);
				}
				
				// Check image at picasa
				Map<String, GphotoEntry> photoEntryCollections = photoMapByAlbumId.get(firstTopic.getId());
				GphotoEntry photoEntry = photoEntryCollections != null ? photoEntryCollections.get(answer.getImageUrl()) : null;
				if(photoEntryCollections == null) photoEntryCollections = new HashMap<String, GphotoEntry>();
				if(photoEntry == null) {
					photoEntry = webClient.uploadImageToAlbum(questionAnswerImagePath.toFile(), null, firstTopic, MD5Checksum.getMD5Checksum(questionAnswerImagePath.toString()));
					photoEntryCollections.put(((MediaContent)photoEntry.getContent()).getUri(), photoEntry);
					photoMapByAlbumId.put(firstTopic.getId(), photoEntryCollections);
				}
				
				answer.setImageUrl( ((MediaContent)photoEntry.getContent()).getUri() );
				answer.setPicasaId( photoEntry.getGphotoId() );
			}
		} catch (IOException | ServiceException e) {
			e.printStackTrace();
		}
		
		return answer;
	}
	
	public static void main(String[] args) {
		new App(args).process();
	}
}