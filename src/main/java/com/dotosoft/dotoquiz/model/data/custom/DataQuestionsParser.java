package com.dotosoft.dotoquiz.model.data.custom;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.model.data.DataQuestions;


public class DataQuestionsParser extends DataQuestions {
	
	public DataQuestionsParser(String id, String mtQuestionType,
			String picasaId, String imagePicasaUrl, String additionalData,
			String question, String isDelete, String correctAnswer,
			String wrongAnswer1, String wrongAnswer2, String wrongAnswer3,
			Date createdDt, String createdBy, String[] topics,
			String isProcessed, APPLICATION_TYPE applicationType) {
		
		super(id, picasaId, imagePicasaUrl, additionalData, question, isDelete, correctAnswer, wrongAnswer1, wrongAnswer2, wrongAnswer3, createdDt, createdBy);
		
		this.questionTypeData = mtQuestionType;
		this.topics = topics;
		this.isProcessed = isProcessed;
		this.applicationType = applicationType;
	}
	
	// only need for batch
	@Transient
	protected String[] topics;

	@Transient
	protected String isProcessed;

	@Transient
	protected APPLICATION_TYPE applicationType;

	@Transient
	protected String questionTypeData;

	public String getAdditionalData() {
		return additionalData;
	}

	public String getQuestionTypeData() {
		return questionTypeData;
	}

	public void setQuestionTypeData(String questionTypeData) {
		this.questionTypeData = questionTypeData;
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
	
	public DataQuestions toDataQuestion()  throws IllegalAccessException, InvocationTargetException {
		DataQuestions datQuestion = new DataQuestions();
		BeanUtils.copyProperties(datQuestion, this);
		return datQuestion;
	}
	
	@Override
	public String toString() {
		return "DataQuestionsParser [id=" + id + ", mtQuestionType=" + mtQuestionType
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
