package com.dotosoft.dotoquiz.model.social;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="sos_topics", catalog="dotoquiz")
public class SocialTopics implements java.io.Serializable {
	@Id
	private String id;
	
	@ManyToOne
	private SocialTopics sosTopics;
	
	@Column(name="name", length=100)
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="image_url", length=200)
	private String imageUrl;
	
	@Column(name="is_delete", length=1)
	private String isDelete;
	
	@Column(name="created_dt", length=19)
	private Date createdDt;
	
	@Column(name="created_by", length=50)
	private String createdBy;

	public SocialTopics() {
	}

	public SocialTopics(String id) {
		this.id = id;
	}

	public SocialTopics(String id, SocialTopics sosTopics, String name,
			String description, String imageUrl, String isDelete,
			Date createdDt, String createdBy) {
		this.id = id;
		this.sosTopics = sosTopics;
		this.name = name;
		this.description = description;
		this.imageUrl = imageUrl;
		this.isDelete = isDelete;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SocialTopics getSosTopics() {
		return this.sosTopics;
	}

	public void setSosTopics(SocialTopics sosTopics) {
		this.sosTopics = sosTopics;
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

}
