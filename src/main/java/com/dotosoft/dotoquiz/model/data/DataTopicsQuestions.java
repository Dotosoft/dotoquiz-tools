package com.dotosoft.dotoquiz.model.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dotosoft.dotoquiz.model.social.SocialTopics;

@Entity
@Table(name="dat_topics_questions", catalog="dotoquiz")
public class DataTopicsQuestions implements java.io.Serializable {

	@Id
	private String id;
	
	@ManyToOne
	private DataQuestions datQuestions;
	
	@ManyToOne
	private SocialTopics sosTopics;
	
	@Column(name="is_delete")
	private String isDelete;
	
	@Column(name="created_dt")
	private Date createdDt;
	
	@Column(name="created_by")
	private String createdBy;

	public DataTopicsQuestions() {
	}

	public DataTopicsQuestions(String id) {
		this.id = id;
	}

	public DataTopicsQuestions(String id, DataQuestions datQuestions,
			SocialTopics sosTopics, String isDelete, Date createdDt,
			String createdBy) {
		this.id = id;
		this.datQuestions = datQuestions;
		this.sosTopics = sosTopics;
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

	public DataQuestions getDatQuestions() {
		return this.datQuestions;
	}

	public void setDatQuestions(DataQuestions datQuestions) {
		this.datQuestions = datQuestions;
	}

	public SocialTopics getSosTopics() {
		return this.sosTopics;
	}

	public void setSosTopics(SocialTopics sosTopics) {
		this.sosTopics = sosTopics;
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
