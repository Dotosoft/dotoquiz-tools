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

package com.dotosoft.tools.quizparser.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.dotosoft.tools.quizparser.config.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.tools.quizparser.representations.QuestionAnswers;
import com.dotosoft.tools.quizparser.representations.Topics;

public class DotoQuizStructure {
	public static QuestionAnswers convertRowToQuestions(Row row, APPLICATION_TYPE type) {
		String pertanyaanId = ReadCellAsString(row.getCell(4), "");
		if(pertanyaanId.equals("")) return null;
		
		String[] topics = ReadCellAsString(row.getCell(5), "").split(";");
		String pertanyaan = ReadCellAsString(row.getCell(6), "");
		String image = ReadCellAsString(row.getCell(7), "");
		String correctAnswer = ReadCellAsString(row.getCell(8), "");
		String wrongAnswer1 = ReadCellAsString(row.getCell(9), "");
		String wrongAnswer2 = ReadCellAsString(row.getCell(10), "");
		String wrongAnswer3 = ReadCellAsString(row.getCell(11), "");
		
		QuestionAnswers qa = new QuestionAnswers(pertanyaanId, topics, pertanyaan, image, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3);
		qa.setApplicationType(type);
		
		return qa;
	}

	public static Topics convertRowToTopics(Row row, APPLICATION_TYPE type) {
		String topicId = ReadCellAsString(row.getCell(0), "");
		if(topicId.equals("")) return null;
		
		String topicName = ReadCellAsString(row.getCell(1), "");
		String topicDescription = ReadCellAsString(row.getCell(2), "");
		String topicParent = ReadCellAsString(row.getCell(3), "");
		
		Topics topic = new Topics(topicId, topicName, topicDescription, topicParent);
		topic.setApplicationType(type);
		
		return topic;
	}
	
	/***
	 * Upload to picasaweb from folder structure and update imageURL
	 * 
	 * @return {@link QuestionAnswers}
	 */
	public static QuestionAnswers uploadImageQuestionAnswer(QuestionAnswers questionAnswer) {
		// TO-DO: get file image from folder structure to picasaweb amd update imageURL for data questionAnswer
		
		return questionAnswer;
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
