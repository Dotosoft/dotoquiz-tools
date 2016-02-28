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

package com.dotosoft.dotoquiz.command.data.metadata;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.dotosoft.dotoquiz.model.parameter.ParameterAchievements;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.APPLICATION_TYPE;

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
