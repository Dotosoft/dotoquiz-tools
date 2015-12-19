package com.dotosoft.tools.dataquizparser.representations;

import java.util.Arrays;
import java.util.List;

import com.dotosoft.tools.dataquizparser.App.APPLICATION_TYPE;

public class QuestionAnswers extends ParserQuizObject {
	private String id;
	private String[] topics;
	private String pertanyaan;
	private String image;
	private String correctAnswer;
	private String wrongAnswer1;
	private String wrongAnswer2;
	private String wrongAnswer3;

	public QuestionAnswers(String id, String[] topics, String pertanyaan,
			String image, String correctAnswer, String wrongAnswer1,
			String wrongAnswer2, String wrongAnswer3) {
		this.id = id;
		this.topics = topics;
		this.pertanyaan = pertanyaan;
		this.image = image;
		this.correctAnswer = correctAnswer;
		this.wrongAnswer1 = wrongAnswer1;
		this.wrongAnswer2 = wrongAnswer2;
		this.wrongAnswer3 = wrongAnswer3;
	}
	
	public String QuestionType() {
		if(image != null && !image.equals("")) {
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
		if(applicationType == APPLICATION_TYPE.BATCH_UPLOAD) {
			return "QuestionAnswers [id=" + id + ", topics=" + Arrays.toString(topics) 
				+ ", pertanyaan=" + pertanyaan + ", image=" + image
				+ ", correctAnswer=" + correctAnswer + ", wrongAnswer1="
				+ wrongAnswer1 + ", wrongAnswer2=" + wrongAnswer2
				+ ", wrongAnswer3=" + wrongAnswer3 + "]";
		} else {
			return "insert into dat_questions(id, question, questionImage, question_type, is_delete, correct_answer, wrong_answer1, wrong_answer2, wrong_answer3, created_by, created_dt) "
					+ "values("
					+ "uuid()"
					+ ", '" + pertanyaan + "'"
					+ ", '" + image + "'"
					+ ", '" + QuestionType() + "'"
					+ ", 'N'"
					+ ", '" + correctAnswer + "'"
					+ ", '" + wrongAnswer1 + "'"
					+ ", '" + wrongAnswer2 + "'"
					+ ", '" + wrongAnswer3 + "'"
					+ ", 'system'"
					+ ", 'current_time'"
					+ ");";
		}
	}

}
