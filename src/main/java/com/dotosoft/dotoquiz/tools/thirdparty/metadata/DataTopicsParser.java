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

package com.dotosoft.dotoquiz.tools.thirdparty.metadata;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.dotosoft.dotoquiz.model.data.DataTopics;
import com.dotosoft.dotoquiz.tools.common.QuizParserConstant.APPLICATION_TYPE;

public class DataTopicsParser extends DataTopics {
	
	public DataTopicsParser(String id, String picasaId, String imagePicasaUrl,
			String topicParentId, String name, String description,
			String imageUrl, String isDelete, Date createdDt, String createdBy,
			String isProcessed, APPLICATION_TYPE applicationType) {
		this.id = id;
		this.picasaId = picasaId;
		this.imagePicasaUrl = imagePicasaUrl;
		this.name = name;
		this.topicParentId = topicParentId;
		this.description = description;
		this.imageUrl = imageUrl;
		this.isDelete = isDelete;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
		this.isProcessed = isProcessed;
		this.applicationType = applicationType;
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
	
	public DataTopics toDataTopics() throws IllegalAccessException, InvocationTargetException {
		DataTopics datTopics = new DataTopics();
		BeanUtils.copyProperties(datTopics, this);
		return datTopics;
	}
	
	@Override
	public String toString() {
		return "DataTopics [id=" + id + ", picasaId=" + picasaId
				+ ", imagePicasaUrl=" + imagePicasaUrl + ", datTopics="
				+ datTopics + ", name=" + name + ", description=" + description
				+ ", imageUrl=" + imageUrl + ", isDelete=" + isDelete
				+ ", createdDt=" + createdDt + ", createdBy=" + createdBy
				+ ", isProcessed=" + isProcessed + ", applicationType="
				+ applicationType + "]";
	}
}
