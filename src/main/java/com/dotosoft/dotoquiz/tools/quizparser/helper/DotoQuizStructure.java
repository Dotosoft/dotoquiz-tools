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

package com.dotosoft.dotoquiz.tools.quizparser.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.dotosoft.dotoquiz.common.DotoQuizConstant;
import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.model.data.custom.DataQuestionsParser;
import com.dotosoft.dotoquiz.model.data.custom.DataTopicsParser;
import com.dotosoft.dotoquiz.model.data.custom.ParameterAchievementParser;
import com.google.gdata.data.spreadsheet.ListEntry;

public class DotoQuizStructure {
	// Excel Data Converter
	public static DataQuestionsParser convertRowExcelToQuestions(Row row, APPLICATION_TYPE type) {
		String pertanyaanId = ReadCellAsString(row.getCell(9), "");
		if(pertanyaanId.equals("")) return null;
		
		String[] topics = ReadCellAsString(row.getCell(10), "").split(";");
		
		String picasaId = ReadCellAsString(row.getCell(11), "");
		String imagePicasaURL = ReadCellAsString(row.getCell(12), "");
		
		String question = ReadCellAsString(row.getCell(13), "");
		String questionType = ReadCellAsString(row.getCell(14), "");
		String additionalData = ReadCellAsString(row.getCell(15), "");
		String correctAnswer = ReadCellAsString(row.getCell(16), "");
		String wrongAnswer1 = ReadCellAsString(row.getCell(17), "");
		String wrongAnswer2 = ReadCellAsString(row.getCell(18), "");
		String wrongAnswer3 = ReadCellAsString(row.getCell(19), "");
		String isProcessed = ReadCellAsString(row.getCell(20), "");
		
		DataQuestionsParser qa = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, DotoQuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
		return qa;
	}

	public static DataTopicsParser convertRowExcelToTopics(Row row, APPLICATION_TYPE type) {
		String topicId = ReadCellAsString(row.getCell(0), "");
		if(topicId.equals("")) return null;
		
		String picasaId = ReadCellAsString(row.getCell(1), "");
		String imagePicasaURL = ReadCellAsString(row.getCell(2), "");
		
		String imageURL = ReadCellAsString(row.getCell(3), "");
		String topicName = ReadCellAsString(row.getCell(4), "");
		String topicDescription = ReadCellAsString(row.getCell(5), "");
		String topicParent = ReadCellAsString(row.getCell(6), "");
		String isProcessed = ReadCellAsString(row.getCell(7), "");
		
		// DataTopics topic = new DataTopics(topicId, picasaId, imagePicasaURL, imageURL, topicName, topicDescription, topicParent, isProcessed);
		DataTopicsParser topic = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return topic;
	}
	
	public static ParameterAchievementParser convertRowExcelToAchievement(Row row, APPLICATION_TYPE type) {
		String achievementId = ReadCellAsString(row.getCell(0), "");
		if(achievementId.equals("")) return null;
		
		String picasaId = ReadCellAsString(row.getCell(1), "");
		String imagePicasaURL = ReadCellAsString(row.getCell(2), "");
		
		String imageURL = ReadCellAsString(row.getCell(3), "");
		String achievementName = ReadCellAsString(row.getCell(4), "");
		String achievementDescription = ReadCellAsString(row.getCell(5), "");
		String isProcessed = ReadCellAsString(row.getCell(6), "");
		
		ParameterAchievementParser achievement = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return achievement;
	}
	
	// Googlesheet Data Converter
	public static DataQuestionsParser convertRowGooglesheetToQuestions(ListEntry row, APPLICATION_TYPE type) {
		// for(String tag : row.getCustomElements().getTags())
		
		String pertanyaanId = row.getCustomElements().getValue("pertanyaanId");
		if(!StringUtils.hasValue(pertanyaanId)) return null;
		
		String[] topics = row.getCustomElements().getValue("topik").split(";");
		
		String picasaId = row.getCustomElements().getValue("photoidpicasa");
		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa_2");
		
		String question = row.getCustomElements().getValue("pertanyaan");
		String questionType = row.getCustomElements().getValue("tipepertanyaan");
		String additionalData = row.getCustomElements().getValue("additionaldata");
		String correctAnswer = row.getCustomElements().getValue("jawabancorrect");
		String wrongAnswer1 = row.getCustomElements().getValue("jawabansalah1");
		String wrongAnswer2 = row.getCustomElements().getValue("jawabansalah2");
		String wrongAnswer3 = row.getCustomElements().getValue("jawabansalah3");
		String isProcessed = row.getCustomElements().getValue("isprocessed_2");
		
		DataQuestionsParser qa = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, DotoQuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
		return qa;
	}

	public static DataTopicsParser convertRowGooglesheetExcelToTopics(ListEntry row, APPLICATION_TYPE type) {
		String topicId = row.getCustomElements().getValue("topikid");
		if(!StringUtils.hasValue(topicId)) return null;
		
		String picasaId = row.getCustomElements().getValue("albumidpicasa");
		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa");
		
		String imageURL = row.getCustomElements().getValue("imageurl");
		String topicName = row.getCustomElements().getValue("topicname");
		String topicDescription = row.getCustomElements().getValue("topicdescription");
		String topicParent = row.getCustomElements().getValue("topicparent");
		String isProcessed = row.getCustomElements().getValue("isprocessed");
		
		DataTopicsParser topic = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return topic;
	}
	
	public static ParameterAchievementParser convertRowGooglesheetExcelToAchievement(ListEntry row, APPLICATION_TYPE type) {
		String achievementId = row.getCustomElements().getValue("achievementid");
		if(!StringUtils.hasValue(achievementId)) return null;
		
		String picasaId = row.getCustomElements().getValue("albumidpicasa");
		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa");
		
		String imageURL = row.getCustomElements().getValue("imageurl");
		String achievementName = row.getCustomElements().getValue("achievementname");
		String achievementDescription = row.getCustomElements().getValue("achievementdescription");
		String isProcessed = row.getCustomElements().getValue("isprocessed");
		
		ParameterAchievementParser topic = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return topic;
	}
	
	private static String ReadCellAsString(Cell cell, String defaultValue) {
		String result = defaultValue;
		if(cell == null) return result;
		
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				result = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
				break;
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
		}
		
		return result;
	}
}
