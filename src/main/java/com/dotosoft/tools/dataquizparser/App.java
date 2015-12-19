package com.dotosoft.tools.dataquizparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.dotosoft.tools.dataquizparser.helper.DotoQuizStructure;
import com.dotosoft.tools.dataquizparser.representations.QuestionAnswers;
import com.dotosoft.tools.dataquizparser.representations.Topics;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.photos.PhotoEntry;

import java.net.URL;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.AuthenticationException;

public class App {
	public enum APPLICATION_TYPE {
		GENERATE_SQL, BATCH_UPLOAD
	}
	
	public static void main(String[] args) {
		try {
			if(args.length != 2) {
				System.out.println("Error: Could not run DataQuizParser.");
				System.out.println("Run: java -jar DataQuizParser.jar [GENERATE_SQL/BATCH_UPLOAD] [File Excel]");
				return;
			}
			
			APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(args[0]);
			
		    FileInputStream file = new FileInputStream(new File(args[1]));
		    XSSFWorkbook workbook = new XSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    
		    List<Topics> topicCollections = new ArrayList<Topics>();
		    List<QuestionAnswers> questionAnswersCollections = new ArrayList<QuestionAnswers>();
		    
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    while(rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        
		        // Extract Topic & QuestionAnswer
		        Topics topic = DotoQuizStructure.convertRowToTopics(row, type);
		        QuestionAnswers questionAnswer = DotoQuizStructure.convertRowToQuestions(row, type);
		        
		        if(topic != null) topicCollections.add(topic);
		        if(questionAnswer != null) questionAnswersCollections.add(questionAnswer);
		        
		        if(type == APPLICATION_TYPE.GENERATE_SQL) {
			        System.out.println(topic);
			        System.out.println(questionAnswer + "\n");
		        }
		    }
		    
		    if(type == APPLICATION_TYPE.BATCH_UPLOAD) {
		    	
		    }
		    
		    file.close();
		     
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}

	}
}