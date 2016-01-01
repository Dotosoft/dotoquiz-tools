package com.dotosoft.dotoquiz.model.parameter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Entity;

@Entity
@Table(name = "mt_achievements", catalog = "dotoquiz")
public class ParameterAchievements implements java.io.Serializable {

	public ParameterAchievements() {
	}

	public ParameterAchievements(String id) {
		this.id = id;
	}

	public ParameterAchievements(String id, String name, String description,
			String isDelete, String imageUrl, String imagePicasaUrl,
			String picasaId, Date createdDt, String createdBy) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isDelete = isDelete;
		this.imageUrl = imageUrl;
		this.imagePicasaUrl = imagePicasaUrl;
		this.picasaId = picasaId;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
	}

	@Id
	protected String id;

	@Column(name = "name")
	protected String name;

	@Column(name = "description")
	protected String description;

	@Column(name = "is_delete")
	protected String isDelete;

	@Column(name = "image_url")
	protected String imageUrl;

	@Column(name = "image_picasa_url")
	protected String imagePicasaUrl;

	@Column(name = "picasaId")
	protected String picasaId;

	@Column(name = "created_dt")
	protected Date createdDt;

	@Column(name = "created_by")
	protected String createdBy;

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getImagePicasaUrl() {
		return imagePicasaUrl;
	}

	public void setImagePicasaUrl(String imagePicasaUrl) {
		this.imagePicasaUrl = imagePicasaUrl;
	}

	public String getPicasaId() {
		return picasaId;
	}

	public void setPicasaId(String picasaId) {
		this.picasaId = picasaId;
	}

	@Override
	public String toString() {
		return "ParameterAchievements [id=" + id + ", name=" + name
				+ ", description=" + description + ", imageUrl=" + imageUrl
				+ ", imagePicasaUrl=" + imagePicasaUrl + ", picasaId="
				+ picasaId + ", createdDt=" + createdDt + ", createdBy="
				+ createdBy + "]";
	}

}
