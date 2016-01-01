package com.dotosoft.dotoquiz.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dat_topics_questions", catalog = "dotoquiz")
public class DataTopicsQuestions implements java.io.Serializable {

	public DataTopicsQuestions(String id, DataQuestions datQuestions,
			DataTopics datTopics, String isDelete, Date createdDt,
			String createdBy) {
		this.id = id;
		this.datQuestions = datQuestions;
		this.datTopics = datTopics;
		this.isDelete = isDelete;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
	}

	@Id
	protected String id;

	@ManyToOne
	protected DataQuestions datQuestions;

	@ManyToOne
	protected DataTopics datTopics;

	@Column(name = "is_delete")
	protected String isDelete;

	@Column(name = "created_dt")
	protected Date createdDt;

	@Column(name = "created_by")
	protected String createdBy;

	public DataTopicsQuestions() {
	}

	public DataTopicsQuestions(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataQuestions getDatQuestions() {
		return this.datQuestions;
	}

	public void setDatQuestions(DataQuestions datQuestions) {
		this.datQuestions = datQuestions;
	}

	public DataTopics getDatTopics() {
		return this.datTopics;
	}

	public void setDatTopics(DataTopics datTopics) {
		this.datTopics = datTopics;
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
