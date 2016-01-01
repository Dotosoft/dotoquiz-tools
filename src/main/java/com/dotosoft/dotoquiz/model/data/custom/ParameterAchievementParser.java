package com.dotosoft.dotoquiz.model.data.custom;

import javax.persistence.Transient;

import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.model.parameter.ParameterAchievements;

public class ParameterAchievementParser extends ParameterAchievements {
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
}
