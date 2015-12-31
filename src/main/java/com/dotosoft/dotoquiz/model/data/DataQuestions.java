package com.dotosoft.dotoquiz.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;

@Entity
@Table(name = "dat_questions", catalog = "dotoquiz")
public class DataQuestions implements java.io.Serializable {

	public DataQuestions(String id, 
			String picasaId, String imagePicasaUrl, String additionalData,
			String question, String isDelete, String correctAnswer,
			String wrongAnswer1, String wrongAnswer2, String wrongAnswer3,
			Date createdDt, String createdBy) {
		
		this.id = id;
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
	}

	@Id
	protected String id;

	@ManyToOne
	protected ParameterQuestionType mtQuestionType;

	@Column(name = "picasaId", length = 100)
	protected String picasaId;

	@Column(name = "imagePicasaUrl", length = 1000)
	protected String imagePicasaUrl;

	@Column(name = "additionalData", length = 1000)
	protected String additionalData;

	@Column(name = "question")
	protected String question;

	@Column(name = "is_delete")
	protected String isDelete;

	@Column(name = "correct_answer")
	protected String correctAnswer;

	@Column(name = "wrong_answer1")
	protected String wrongAnswer1;

	@Column(name = "wrong_answer2")
	protected String wrongAnswer2;

	@Column(name = "wrong_answer3")
	protected String wrongAnswer3;

	@Column(name = "created_dt")
	protected Date createdDt;

	@Column(name = "created_by")
	protected String createdBy;

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
				+ ", createdBy=" + createdBy + "]";
	}

}
