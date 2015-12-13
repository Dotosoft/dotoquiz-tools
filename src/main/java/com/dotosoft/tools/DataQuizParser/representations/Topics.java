package com.dotosoft.tools.DataQuizParser.representations;

import com.dotosoft.tools.DataQuizParser.App.APPLICATION_TYPE;

public class Topics extends ParserQuizObject {
	private String id;
	private String topicName;
	private String topicDescription;
	private String topicParentId;

	public Topics(String id, String topicName, String topicDescription,
			String topicParentId) {
		super();
		this.id = id;
		this.topicName = topicName;
		this.topicDescription = topicDescription;
		this.topicParentId = topicParentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTopicDescription() {
		return topicDescription;
	}

	public void setTopicDescription(String topicDescription) {
		this.topicDescription = topicDescription;
	}

	public String getTopicParentId() {
		return topicParentId;
	}

	public void setTopicParentId(String topicParentId) {
		this.topicParentId = topicParentId;
	}

	@Override
	public String toString() {
		if (applicationType == APPLICATION_TYPE.BATCH_UPLOAD) {
			return "Topics [id=" + id + ", topicName=" + topicName + ", topicParentId=" + topicParentId + ", topicDescription=" + topicDescription + "]";
		} else {
			return "insert into sos_topics(id, name, description, image_url, is_delete, created_dt, created_by, topicParent) " 
					+ "values ("
					+ "uuid(), "
					+ "'" + topicName 
					+ "', '" + topicDescription + "'"
					+ "', 'topic.jpg'"
					+ "', 'N'"
					+ "', current_time"
					+ "', 'system'"
					+ "', '" + topicParentId + "'"
					+ ");";
		}
	}
}
