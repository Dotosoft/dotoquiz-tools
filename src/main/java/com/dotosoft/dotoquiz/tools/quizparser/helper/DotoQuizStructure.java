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

import java.lang.reflect.Field;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.dotosoft.dotoquiz.common.DotoQuizConstant;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.tools.quizparser.common.QuizParserConstant.DATA_TYPE;
import com.dotosoft.dotoquiz.tools.quizparser.config.Settings;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.DataQuestionsParser;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.DataTopicsParser;
import com.dotosoft.dotoquiz.tools.quizparser.data.custom.ParameterAchievementParser;
import com.dotosoft.dotoquiz.utils.StringUtils;
import com.google.gdata.data.spreadsheet.ListEntry;

public class DotoQuizStructure {
//	// Excel Data Converter
//	public static DataQuestionsParser convertRowExcelToQuestions(Row row, Settings setting) {
//		String pertanyaanId = readCellAsString(row.getCell(9), "");
//		if(pertanyaanId.equals("")) return null;
//		
//		String[] topics = readCellAsString(row.getCell(10), "").split(";");
//		
//		String picasaId = readCellAsString(row.getCell(11), "");
//		String imagePicasaURL = readCellAsString(row.getCell(12), "");
//		
//		String question = readCellAsString(row.getCell(13), "");
//		String questionType = readCellAsString(row.getCell(14), "");
//		String additionalData = readCellAsString(row.getCell(15), "");
//		String correctAnswer = readCellAsString(row.getCell(16), "");
//		String wrongAnswer1 = readCellAsString(row.getCell(17), "");
//		String wrongAnswer2 = readCellAsString(row.getCell(18), "");
//		String wrongAnswer3 = readCellAsString(row.getCell(19), "");
//		String isProcessed = readCellAsString(row.getCell(20), "");
//		
//		DataQuestionsParser qa = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, DotoQuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
//		return qa;
//	}
//
//	public static DataTopicsParser convertRowExcelToTopics(Row row, Settings setting) {
//		String topicId = readCellAsString(row.getCell(0), "");
//		if(topicId.equals("")) return null;
//		
//		String picasaId = readCellAsString(row.getCell(1), "");
//		String imagePicasaURL = readCellAsString(row.getCell(2), "");
//		
//		String imageURL = readCellAsString(row.getCell(3), "");
//		String topicName = readCellAsString(row.getCell(4), "");
//		String topicDescription = readCellAsString(row.getCell(5), "");
//		String topicParent = readCellAsString(row.getCell(6), "");
//		String isProcessed = readCellAsString(row.getCell(7), "");
//		
//		// DataTopics topic = new DataTopics(topicId, picasaId, imagePicasaURL, imageURL, topicName, topicDescription, topicParent, isProcessed);
//		DataTopicsParser topic = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
//		return topic;
//	}
//	
//	public static ParameterAchievementParser convertRowExcelToAchievement(Row row, Settings setting) {
//		String achievementId = readCellAsString(row.getCell(0), "");
//		if(achievementId.equals("")) return null;
//		
//		String picasaId = readCellAsString(row.getCell(1), "");
//		String imagePicasaURL = readCellAsString(row.getCell(2), "");
//		
//		String imageURL = readCellAsString(row.getCell(3), "");
//		String achievementName = readCellAsString(row.getCell(4), "");
//		String achievementDescription = readCellAsString(row.getCell(5), "");
//		String isProcessed = readCellAsString(row.getCell(6), "");
//		
//		ParameterAchievementParser achievement = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
//		return achievement;
//	}
//	
//	// Googlesheet Data Converter
//	public static DataQuestionsParser convertRowGooglesheetToQuestions(ListEntry row, Settings setting) {
//
//		String pertanyaanId = row.getCustomElements().getValue("pertanyaanId");
//		if(!StringUtils.hasValue(pertanyaanId)) return null;
//		
//		String[] topics = row.getCustomElements().getValue("topik").split(";");
//		
//		String picasaId = row.getCustomElements().getValue("photoidpicasa");
//		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa_2");
//		
//		String question = row.getCustomElements().getValue("pertanyaan");
//		String questionType = row.getCustomElements().getValue("tipepertanyaan");
//		String additionalData = row.getCustomElements().getValue("additionaldata");
//		String correctAnswer = row.getCustomElements().getValue("jawabancorrect");
//		String wrongAnswer1 = row.getCustomElements().getValue("jawabansalah1");
//		String wrongAnswer2 = row.getCustomElements().getValue("jawabansalah2");
//		String wrongAnswer3 = row.getCustomElements().getValue("jawabansalah3");
//		String isProcessed = row.getCustomElements().getValue("isprocessed_2");
//		
//		DataQuestionsParser qa = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, DotoQuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
//		return qa;
//	}
	
	public static DataTopicsParser convertDataToTopics(Object data, Settings setting) {
		String topicId = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicId", data);
		if(!StringUtils.hasValue(topicId)) return null;
		
		String picasaId = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iPicasaId", data);
		String imagePicasaURL = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iImagePicasaURL", data);
		
		String imageURL = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iImageURL", data);
		String topicName = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicName", data);
		String topicDescription = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicDescription", data);
		String topicParent = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicParent", data);
		String isProcessed = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iIsProcessed", data);
		
		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(setting.getApplicationType());
		DataTopicsParser result = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return result;
	}
	
	public static DataQuestionsParser convertDataToAnswerQuestion(Object data, Settings setting) {
		String pertanyaanId = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iPertanyaanId", data);
		if(!StringUtils.hasValue(pertanyaanId)) return null;
		
		String[] topics = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iTopics", data).split(";");
		
		String picasaId = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iPicasaId", data);
		String imagePicasaURL = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iImagePicasaURL", data);
		
		String question = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iQuestion", data);
		String questionType = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iQuestionType", data);
		String additionalData = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iAdditionalData", data);
		String correctAnswer = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iCorrectAnswer", data);
		String wrongAnswer1 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer1", data);
		String wrongAnswer2 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer2", data);
		String wrongAnswer3 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer3", data);
		String isProcessed = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iIsProcessed", data);
		
		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(setting.getApplicationType());
		DataQuestionsParser result = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, DotoQuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
		return result;
	}
	
	public static ParameterAchievementParser convertDataToAchievement(Object data, Settings setting) {
		String achievementId = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iAchievementId", data);
		if(!StringUtils.hasValue(achievementId)) return null;
		
		String picasaId = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iAlbumIdPicasa", data);
		String imagePicasaURL = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iImageURLPicasa", data);
		
		String imageURL = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iImageURL", data);
		String achievementName = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iAchievementName", data);
		String achievementDescription = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iAchievementDescription", data);
		String isProcessed = getValueFromRowData(QuizParserConstant.PARSE_ACHIEVEMENT, setting, "iIsProcessed", data);
		
		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(setting.getApplicationType());
		ParameterAchievementParser result = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
		return result;
	}

//	public static DataTopicsParser convertRowGooglesheetExcelToTopics(ListEntry row, Settings setting) {
//		String topicId = row.getCustomElements().getValue("topikid");
//		if(!StringUtils.hasValue(topicId)) return null;
//		
//		String picasaId = row.getCustomElements().getValue("albumidpicasa");
//		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa");
//		
//		String imageURL = row.getCustomElements().getValue("imageurl");
//		String topicName = row.getCustomElements().getValue("topicname");
//		String topicDescription = row.getCustomElements().getValue("topicdescription");
//		String topicParent = row.getCustomElements().getValue("topicparent");
//		String isProcessed = row.getCustomElements().getValue("isprocessed");
//		
//		DataTopicsParser topic = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
//		return topic;
//	}
//	
//	public static ParameterAchievementParser convertRowGooglesheetExcelToAchievement(ListEntry row, Settings setting) {
//		String achievementId = row.getCustomElements().getValue("achievementid");
//		if(!StringUtils.hasValue(achievementId)) return null;
//		
//		String picasaId = row.getCustomElements().getValue("albumidpicasa");
//		String imagePicasaURL = row.getCustomElements().getValue("imageurlpicasa");
//		
//		String imageURL = row.getCustomElements().getValue("imageurl");
//		String achievementName = row.getCustomElements().getValue("achievementname");
//		String achievementDescription = row.getCustomElements().getValue("achievementdescription");
//		String isProcessed = row.getCustomElements().getValue("isprocessed");
//		
//		ParameterAchievementParser topic = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, DotoQuizConstant.NO, new java.util.Date(), DotoQuizConstant.SYSTEM_USER, isProcessed, type);
//		return topic;
//	}
	
	private static String getValueFromRowData(String parseType, Settings setting, String key, Object data) {
		String paramKey = getStructureKey(parseType, setting, key);
		String result = "";
		if( DATA_TYPE.EXCEL.toString().equals(setting.getDataType()) ) {
			Row row = (Row) data;
			result = readCellAsString(row.getCell(Integer.parseInt(paramKey)), "");
		} else if( DATA_TYPE.GOOGLESHEET.toString().equals(setting.getDataType()) ) {
			ListEntry row = (ListEntry) data;
			result = row.getCustomElements().getValue(paramKey);
		}
		
		return result;
	}
	
	private static String getStructureKey(String parseType, Settings setting, String key) {
		String result = "";
		try {
			String data = "";
			if(QuizParserConstant.PARSE_TOPIC.equals(parseType)) {
				data = (String) PropertyUtils.getProperty(setting.getTopicStructure(), key);
			} else if(QuizParserConstant.PARSE_QUESTION_ANSWER.equals(parseType)) {
				data = (String) PropertyUtils.getProperty(setting.getAnswerQuestionStructure(), key);
			} else if(QuizParserConstant.PARSE_ACHIEVEMENT.equals(parseType)) {
				data = (String) PropertyUtils.getProperty(setting.getAchievementStructure(), key);
			}
			
			if(DATA_TYPE.EXCEL.toString().equals(setting.getDataType())) {
				result = data.split(";")[0];
			} else if(DATA_TYPE.GOOGLESHEET.toString().equals(setting.getDataType())) {
				result = data.split(";")[1];
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
//	private static String getValueDynamic(Object object, String fieldName) throws NoSuchFieldException, SecurityException, InstantiationException, IllegalAccessException {
//		Field field = object.getClass().getDeclaredField(fieldName);
//		field.setAccessible(true);
//
//		Class<?> targetType = field.getType();
//		Object objectValue = targetType.newInstance();
//
//		return (String) field.get(objectValue);
//	}
	
	private static String readCellAsString(Cell cell, String defaultValue) {
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
