package com.dotosoft.dotoquiz.model.data;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;
import com.dotosoft.dotoquiz.tools.quizparser.config.QuizParserConstant.APPLICATION_TYPE;

@Entity
@Table(name = "dat_questions", catalog = "dotoquiz")
public class DataQuestions implements java.io.Serializable {

	public DataQuestions(String id, ParameterQuestionType mtQuestionType,
			String picasaId, String imagePicasaUrl, String additionalData,
			String question, String isDelete, String correctAnswer,
			String wrongAnswer1, String wrongAnswer2, String wrongAnswer3,
			Date createdDt, String createdBy, String[] topics,
			String isProcessed, APPLICATION_TYPE applicationType) {
		this.id = id;
		this.mtQuestionType = mtQuestionType;
		this.picasaId = picasaId;
		this.imagePicasaUrl = imagePicasaUrl;
		this.additionalData = additionalData;
		this.question = question;
		this.isDelete = isDelete;
		this.correctAnswer = correctAnswer;
		this.wrongAnswer1 = wrongAnswer1;
		this.wrongAnswer2 = wrongAnswer2;
		this.wrongAnswer3 = wrongAnswer3;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
		this.topics = topics;
		this.isProcessed = isProcessed;
		this.applicationType = applicationType;
	}

	@Id
	private String id;

	@ManyToOne
	private ParameterQuestionType mtQuestionType;

	@Column(name = "picasaId", length = 100)
	private String picasaId;

	@Column(name = "imagePicasaUrl", length = 1000)
	private String imagePicasaUrl;

	@Column(name = "additionalData", length = 1000)
	private String additionalData;

	@Column(name = "question")
	private String question;

	@Column(name = "is_delete")
	private String isDelete;

	@Column(name = "correct_answer")
	private String correctAnswer;

	@Column(name = "wrong_answer1")
	private String wrongAnswer1;

	@Column(name = "wrong_answer2")
	private String wrongAnswer2;

	@Column(name = "wrong_answer3")
	private String wrongAnswer3;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "created_by")
	private String createdBy;

	// only need for batch
	private String[] topics;
	private String isProcessed;
	private APPLICATION_TYPE applicationType;

	public DataQuestions() {
	}

	public DataQuestions(String id) {
		this.id = id;
	}

	public String getPicasaId() {
		return picasaId;
	}

	public void setPicasaId(String picasaId) {
		this.picasaId = picasaId;
	}

	public String getImagePicasaUrl() {
		return imagePicasaUrl;
	}

	public void setImagePicasaUrl(String imagePicasaUrl) {
		this.imagePicasaUrl = imagePicasaUrl;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String[] getTopics() {
		return topics;
	}

	public void setTopics(String[] topics) {
		this.topics = topics;
	}

	public String getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(String isProcessed) {
		this.isProcessed = isProcessed;
	}

	public APPLICATION_TYPE getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(APPLICATION_TYPE applicationType) {
		this.applicationType = applicationType;
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ParameterQuestionType getMtQuestionType() {
		return this.mtQuestionType;
	}

	public void setMtQuestionType(ParameterQuestionType mtQuestionType) {
		this.mtQuestionType = mtQuestionType;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "DataQuestions [id=" + id + ", mtQuestionType=" + mtQuestionType
				+ ", picasaId=" + picasaId + ", imagePicasaUrl="
				+ imagePicasaUrl + ", additionalData=" + additionalData
				+ ", question=" + question + ", isDelete=" + isDelete
				+ ", correctAnswer=" + correctAnswer + ", wrongAnswer1="
				+ wrongAnswer1 + ", wrongAnswer2=" + wrongAnswer2
				+ ", wrongAnswer3=" + wrongAnswer3 + ", createdDt=" + createdDt
				+ ", createdBy=" + createdBy + ", topics="
				+ Arrays.toString(topics) + ", isProcessed=" + isProcessed
				+ ", applicationType=" + applicationType + "]";
	}

}
