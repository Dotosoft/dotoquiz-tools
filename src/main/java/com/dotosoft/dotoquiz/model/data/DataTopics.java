package com.dotosoft.dotoquiz.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dotosoft.dotoquiz.tools.quizparser.config.QuizParserConstant.APPLICATION_TYPE;

@Entity
@Table(name = "dat_topics", catalog = "dotoquiz")
public class DataTopics implements java.io.Serializable {
	public DataTopics(String id, String picasaId, String imagePicasaUrl,
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

	@Id
	private String id;

	@Column(name = "picasaId", length = 100)
	private String picasaId;

	@Column(name = "imagePicasaUrl", length = 1000)
	private String imagePicasaUrl;

	@ManyToOne
	private DataTopics datTopics;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "image_url", length = 200)
	private String imageUrl;

	@Column(name = "is_delete", length = 1)
	private String isDelete;

	@Column(name = "created_dt", length = 19)
	private Date createdDt;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	// only need for batch
	@Transient
	private String isProcessed;
	@Transient
	private APPLICATION_TYPE applicationType;
	@Transient
	private String topicParentId;

	public String getTopicParentId() {
		return topicParentId;
	}

	public void setTopicParentId(String topicParentId) {
		this.topicParentId = topicParentId;
	}

	public DataTopics() {
	}

	public DataTopics(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataTopics getSosTopics() {
		return this.datTopics;
	}

	public void setSosTopics(DataTopics datTopics) {
		this.datTopics = datTopics;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getIsProcessed() {
		return isProcessed;
	}

	public void setIsProcessed(String isProcessed) {
		this.isProcessed = isProcessed;
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

	public DataTopics getDatTopics() {
		return datTopics;
	}

	public void setDatTopics(DataTopics datTopics) {
		this.datTopics = datTopics;
	}

	public APPLICATION_TYPE getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(APPLICATION_TYPE applicationType) {
		this.applicationType = applicationType;
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
