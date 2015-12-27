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

package com.dotosoft.tools.quizparser.representations;

import java.util.Arrays;

import com.dotosoft.tools.quizparser.config.QuizParserConstant.APPLICATION_TYPE;

public class QuestionAnswers extends ParserQuizObject {
	private String id;
	private String picasaId;
	private String[] topics;
	private String pertanyaan;
	private String imageUrl;
	private String correctAnswer;
	private String wrongAnswer1;
	private String wrongAnswer2;
	private String wrongAnswer3;

	public QuestionAnswers(String id, String[] topics, String pertanyaan,
			String imageUrl, String correctAnswer, String wrongAnswer1,
			String wrongAnswer2, String wrongAnswer3) {
		this.id = id;
		this.topics = topics;
		this.pertanyaan = pertanyaan;
		this.imageUrl = imageUrl;
		this.correctAnswer = correctAnswer;
		this.wrongAnswer1 = wrongAnswer1;
		this.wrongAnswer2 = wrongAnswer2;
		this.wrongAnswer3 = wrongAnswer3;
	}

	public String getPicasaId() {
		return picasaId;
	}

	public void setPicasaId(String picasaId) {
		this.picasaId = picasaId;
	}

	public String QuestionType() {
		if (imageUrl != null && !imageUrl.equals("")) {
			return "Text/HTML";
		} else {
			return "Plain Text";
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getPertanyaan() {
		return pertanyaan;
	}

	public void setPertanyaan(String pertanyaan) {
		this.pertanyaan = pertanyaan;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getWrongAnswer1() {
		return wrongAnswer1;
	}

	public void setWrongAnswer1(String wrongAnswer1) {
		this.wrongAnswer1 = wrongAnswer1;
	}

	public String getWrongAnswer2() {
		return wrongAnswer2;
	}

	public void setWrongAnswer2(String wrongAnswer2) {
		this.wrongAnswer2 = wrongAnswer2;
	}

	public String getWrongAnswer3() {
		return wrongAnswer3;
	}

	public void setWrongAnswer3(String wrongAnswer3) {
		this.wrongAnswer3 = wrongAnswer3;
	}

	@Override
	public String toString() {
		if (applicationType == APPLICATION_TYPE.BATCH_UPLOAD) {
			return "QuestionAnswers [id=" + id + ", picasaId=" + picasaId
					+ ", topics=" + Arrays.toString(topics) + ", pertanyaan="
					+ pertanyaan + ", imageUrl=" + imageUrl + ", correctAnswer="
					+ correctAnswer + ", wrongAnswer1=" + wrongAnswer1
					+ ", wrongAnswer2=" + wrongAnswer2 + ", wrongAnswer3="
					+ wrongAnswer3 + "]";
		} else {
			return "insert into dat_questions(id, picasaId, question, questionImage, question_type, is_delete, correct_answer, wrong_answer1, wrong_answer2, wrong_answer3, created_by, created_dt) "
				+ "values(" + "uuid()"
				+ ", '" + picasaId
				+ "', '" + pertanyaan
				+ "', '" + imageUrl
				+ "', '" + QuestionType()
				+ "', 'N'"
				+ ", '" + correctAnswer 
				+ "', '" + wrongAnswer1
				+ "', '" + wrongAnswer2
				+ "', '"+ wrongAnswer3
				+ "', 'system'"
				+ ", current_time" + ");";
		}
	}
}
