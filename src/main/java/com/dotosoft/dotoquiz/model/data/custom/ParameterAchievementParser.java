package com.dotosoft.dotoquiz.model.data.custom;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.dotosoft.dotoquiz.common.DotoQuizConstant;
import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.model.parameter.ParameterAchievements;

public class ParameterAchievementParser extends ParameterAchievements {
	public ParameterAchievementParser(String id, String picasaId,
			String imagePicasaURL, String name, String description,
			String imageURL, String isDelete, Date createdDt, String createdBy,
			String isProcessed, APPLICATION_TYPE type) {
		super(id, name, description, isDelete, imageURL, imagePicasaURL,
				picasaId, createdDt, createdBy);
		this.isProcessed = isProcessed;
		this.applicationType = applicationType;
		this.topicParentId = topicParentId;
	}

	// only need for batch
	@Transient
	protected String isProcessed;

	@Transient
	protected APPLICATION_TYPE applicationType;

	@Transient
	protected String topicParentId;

	public String getTopicParentId() {
		return topicParentId;
	}

	public void setTopicParentId(String topicParentId) {
		this.topicParentId = topicParentId;
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
	
	public ParameterAchievements toParameterAchievements() throws IllegalAccessException, InvocationTargetException {
		ParameterAchievements achievement = new ParameterAchievements();
		BeanUtils.copyProperties(achievement, this);
		return achievement;
	}

	@Override
	public String toString() {
		return "ParameterAchievementParser [isProcessed=" + isProcessed
				+ ", applicationType=" + applicationType + ", topicParentId="
				+ topicParentId + "]";
	}

}
