package com.dotosoft.dotoquiz.model.parameter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mt_question_type", catalog = "dotoquiz")
public class ParameterQuestionType implements java.io.Serializable {

	@Id
	protected String id;

	@Column(name = "name", length = 45)
	protected String name;

	@Column(name = "description")
	protected String description;

	@Column(name = "is_delete", length = 1)
	protected String isDelete;

	@Column(name = "created_dt", length = 19)
	protected Date createdDt;

	@Column(name = "created_by", length = 50)
	protected String createdBy;

	public ParameterQuestionType() {
	}

	public ParameterQuestionType(String id) {
		this.id = id;
	}

	public ParameterQuestionType(String id, String name, String description,
			String isDelete, Date createdDt, String createdBy) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	@Override
	public String toString() {
		return "ParameterQuestionType [id=" + id + ", name=" + name
				+ ", description=" + description + ", isDelete=" + isDelete
				+ ", createdDt=" + createdDt + ", createdBy=" + createdBy + "]";
	}

}
