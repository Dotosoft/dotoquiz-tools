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

package com.dotosoft.dotoquiz.tools.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.dotosoft.dotoquiz.common.QuizConstant;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.DATA_TYPE;
import com.dotosoft.dotoquiz.tools.config.Settings;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.DataQuestionsParser;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.DataTopicsParser;
import com.dotosoft.dotoquiz.tools.thirdparty.metadata.ParameterAchievementParser;
import com.dotosoft.dotoquiz.utils.StringUtils;
import com.google.gdata.data.spreadsheet.ListEntry;

public class DotoQuizStructure {
	
	public static DataTopicsParser convertDataToTopics(Object data, Settings setting) {
		String topicId = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicId", data);
		if(!StringUtils.hasValue(topicId)) return null;
		
		String picasaId = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iAlbumIdPicasa", data);
		String imagePicasaURL = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iImageURLPicasa", data);
		
		String imageURL = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iImageURL", data);
		String topicName = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicName", data);
		String topicDescription = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicDescription", data);
		String topicParent = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iTopicParent", data);
		String isProcessed = getValueFromRowData(QuizParserConstant.PARSE_TOPIC, setting, "iIsProcessed", data);
		
		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(setting.getApplicationType());
		DataTopicsParser result = new DataTopicsParser(topicId, picasaId, imagePicasaURL, topicParent, topicName, topicDescription, imageURL, QuizConstant.NO, new java.util.Date(), QuizConstant.SYSTEM_USER, isProcessed, type);
		return result;
	}
	
	public static DataQuestionsParser convertDataToAnswerQuestion(Object data, Settings setting) {
		String pertanyaanId = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iPertanyaanId", data);
		if(!StringUtils.hasValue(pertanyaanId)) return null;
		
		String[] topics = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iTopics", data).split(";");
		
		String picasaId = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iAlbumIdPicasa", data);
		String imagePicasaURL = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iImageURLPicasa", data);
		
		String question = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iQuestion", data);
		String questionType = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iQuestionType", data);
		String additionalData = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iAdditionalData", data);
		String correctAnswer = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iCorrectAnswer", data);
		String wrongAnswer1 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer1", data);
		String wrongAnswer2 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer2", data);
		String wrongAnswer3 = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iWrongAnswer3", data);
		String isProcessed = getValueFromRowData(QuizParserConstant.PARSE_QUESTION_ANSWER, setting, "iIsProcessed", data);
		
		APPLICATION_TYPE type = APPLICATION_TYPE.valueOf(setting.getApplicationType());
		DataQuestionsParser result = new DataQuestionsParser(pertanyaanId, questionType, picasaId, imagePicasaURL, additionalData, questionType, QuizConstant.NO, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, new java.util.Date(), "system", topics, isProcessed, type);
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
		ParameterAchievementParser result = new ParameterAchievementParser(achievementId, picasaId, imagePicasaURL, achievementName, achievementDescription, imageURL, QuizConstant.NO, new java.util.Date(), QuizConstant.SYSTEM_USER, isProcessed, type);
		return result;
	}
	
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
	
	public static String getStructureKey(String parseType, Settings setting, String key) {
		String result = "";
		try {
			String data = "";
			if(QuizParserConstant.PARSE_TOPIC.equals(parseType)) {
				data = (String) BeanUtils.getProperty(setting.getStructure().getTopicStructure(), key);
			} else if(QuizParserConstant.PARSE_QUESTION_ANSWER.equals(parseType)) {
				data = (String) BeanUtils.getProperty(setting.getStructure().getAnswerQuestionStructure(), key);
			} else if(QuizParserConstant.PARSE_ACHIEVEMENT.equals(parseType)) {
				data = (String) BeanUtils.getProperty(setting.getStructure().getAchievementStructure(), key);
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
