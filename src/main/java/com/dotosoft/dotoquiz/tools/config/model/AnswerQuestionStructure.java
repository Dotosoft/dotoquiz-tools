package com.dotosoft.dotoquiz.tools.config.model;

public class AnswerQuestionStructure {
	private String iPertanyaanId;
	private String iTopics;
	private String iAlbumIdPicasa;
	private String iImageURLPicasa;
	private String iQuestion;
	private String iQuestionType;
	private String iAdditionalData;
	private String iCorrectAnswer;
	private String iWrongAnswer1;
	private String iWrongAnswer2;
	private String iWrongAnswer3;
	private String iIsProcessed;

	public String getiPertanyaanId() {
		return iPertanyaanId;
	}

	public void setiPertanyaanId(String iPertanyaanId) {
		this.iPertanyaanId = iPertanyaanId;
	}

	public String getiTopics() {
		return iTopics;
	}

	public void setiTopics(String iTopics) {
		this.iTopics = iTopics;
	}

	public String getiAlbumIdPicasa() {
		return iAlbumIdPicasa;
	}

	public void setiAlbumIdPicasa(String iAlbumIdPicasa) {
		this.iAlbumIdPicasa = iAlbumIdPicasa;
	}

	public String getiImageURLPicasa() {
		return iImageURLPicasa;
	}

	public void setiImageURLPicasa(String iImageURLPicasa) {
		this.iImageURLPicasa = iImageURLPicasa;
	}

	public String getiQuestion() {
		return iQuestion;
	}

	public void setiQuestion(String iQuestion) {
		this.iQuestion = iQuestion;
	}

	public String getiQuestionType() {
		return iQuestionType;
	}

	public void setiQuestionType(String iQuestionType) {
		this.iQuestionType = iQuestionType;
	}

	public String getiAdditionalData() {
		return iAdditionalData;
	}

	public void setiAdditionalData(String iAdditionalData) {
		this.iAdditionalData = iAdditionalData;
	}

	public String getiCorrectAnswer() {
		return iCorrectAnswer;
	}

	public void setiCorrectAnswer(String iCorrectAnswer) {
		this.iCorrectAnswer = iCorrectAnswer;
	}

	public String getiWrongAnswer1() {
		return iWrongAnswer1;
	}

	public void setiWrongAnswer1(String iWrongAnswer1) {
		this.iWrongAnswer1 = iWrongAnswer1;
	}

	public String getiWrongAnswer2() {
		return iWrongAnswer2;
	}

	public void setiWrongAnswer2(String iWrongAnswer2) {
		this.iWrongAnswer2 = iWrongAnswer2;
	}

	public String getiWrongAnswer3() {
		return iWrongAnswer3;
	}

	public void setiWrongAnswer3(String iWrongAnswer3) {
		this.iWrongAnswer3 = iWrongAnswer3;
	}

	public String getiIsProcessed() {
		return iIsProcessed;
	}

	public void setiIsProcessed(String iIsProcessed) {
		this.iIsProcessed = iIsProcessed;
	}

	@Override
	public String toString() {
		return "AnswerQuestionStructure [iPertanyaanId=" + iPertanyaanId
				+ ", iTopics=" + iTopics + ", iAlbumIdPicasa=" + iAlbumIdPicasa
				+ ", iImageURLPicasa=" + iImageURLPicasa + ", iQuestion="
				+ iQuestion + ", iQuestionType=" + iQuestionType
				+ ", iAdditionalData=" + iAdditionalData + ", iCorrectAnswer="
				+ iCorrectAnswer + ", iWrongAnswer1=" + iWrongAnswer1
				+ ", iWrongAnswer2=" + iWrongAnswer2 + ", iWrongAnswer3="
				+ iWrongAnswer3 + ", iIsProcessed=" + iIsProcessed + "]";
	}

}
