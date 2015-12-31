package com.dotosoft.dotoquiz.model.data.custom;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;

import com.dotosoft.dotoquiz.common.QuizParserConstant.APPLICATION_TYPE;
import com.dotosoft.dotoquiz.model.data.DataTopics;

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
