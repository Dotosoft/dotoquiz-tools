package com.dotosoft.dotoquiz.model.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;

@Entity
@Table(name = "dat_questions", catalog = "dotoquiz")
public class DataQuestions implements java.io.Serializable {

	@Id
	private String id;

	@ManyToOne
	private ParameterQuestionType mtQuestionType;

	@Column(name = "question")
	private String question;

	@Column(name = "is_delete")
	private String isDelete;

	@Column(name = "correct_answer")
	private String correctAnswer;

	@Column(name = "wrong_answer1")
	private String wrongAnswer1;

	@Column(name = "wrong_answer2")
	private String wrongAnswer2;

	@Column(name = "wrong_answer3")
	private String wrongAnswer3;

	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "created_by")
	private String createdBy;

	public DataQuestions() {
	}

	public DataQuestions(String id) {
		this.id = id;
	}

	public DataQuestions(String id, ParameterQuestionType mtQuestionType,
			String question, String isDelete, String correctAnswer,
			String wrongAnswer1, String wrongAnswer2, String wrongAnswer3,
			Date createdDt, String createdBy) {
		super();
		this.id = id;
		this.mtQuestionType = mtQuestionType;
		this.question = question;
		this.isDelete = isDelete;
		this.correctAnswer = correctAnswer;
		this.wrongAnswer1 = wrongAnswer1;
		this.wrongAnswer2 = wrongAnswer2;
		this.wrongAnswer3 = wrongAnswer3;
		this.createdDt = createdDt;
		this.createdBy = createdBy;
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String getWrongAnswer1() {
		return wrongAnswer1;
	}

	public void setWrongAnswer1(String wrongAnswer1) {
		this.wrongAnswer1 = wrongAnswer1;
	}

	public String getWrongAnswer2() {
		return wrongAnswer2;
	}

	public void setWrongAnswer2(String wrongAnswer2) {
		this.wrongAnswer2 = wrongAnswer2;
	}

	public String getWrongAnswer3() {
		return wrongAnswer3;
	}

	public void setWrongAnswer3(String wrongAnswer3) {
		this.wrongAnswer3 = wrongAnswer3;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ParameterQuestionType getMtQuestionType() {
		return this.mtQuestionType;
	}

	public void setMtQuestionType(ParameterQuestionType mtQuestionType) {
		this.mtQuestionType = mtQuestionType;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
