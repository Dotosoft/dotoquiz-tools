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

package com.dotosoft.dotoquiz.tools.quizparser.representations;

//import com.dotosoft.dotoquiz.tools.quizparser.config.QuizParserConstant.APPLICATION_TYPE;
//
//public class Topics extends ParserQuizObject {

//	private String id;
//	private String picasaId;
//	private String imagePicasaUrl;
//	private String imageUrl;
//	private String topicName;
//	private String topicDescription;
//	private String topicParentId;
//	private String isProcessed;
//	
//	public Topics(String id, String picasaId, String imagePicasaUrl,
//			String imageUrl, String topicName, String topicDescription,
//			String topicParentId, String isProcessed) {
//		this.id = id;
//		this.picasaId = picasaId;
//		this.imagePicasaUrl = imagePicasaUrl;
//		this.imageUrl = imageUrl;
//		this.topicName = topicName;
//		this.topicDescription = topicDescription;
//		this.topicParentId = topicParentId;
//		this.isProcessed = isProcessed;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getPicasaId() {
//		return picasaId;
//	}
//
//	public void setPicasaId(String picasaId) {
//		this.picasaId = picasaId;
//	}
//
//	public String getImagePicasaUrl() {
//		return imagePicasaUrl;
//	}
//
//	public void setImagePicasaUrl(String imagePicasaUrl) {
//		this.imagePicasaUrl = imagePicasaUrl;
//	}
//
//	public String getImageUrl() {
//		return imageUrl;
//	}
//
//	public void setImageUrl(String imageUrl) {
//		this.imageUrl = imageUrl;
//	}
//
//	public String getTopicName() {
//		return topicName;
//	}
//
//	public void setTopicName(String topicName) {
//		this.topicName = topicName;
//	}
//
//	public String getTopicDescription() {
//		return topicDescription;
//	}
//
//	public void setTopicDescription(String topicDescription) {
//		this.topicDescription = topicDescription;
//	}
//
//	public String getTopicParentId() {
//		return topicParentId;
//	}
//
//	public void setTopicParentId(String topicParentId) {
//		this.topicParentId = topicParentId;
//	}
//
//	public String getIsProcessed() {
//		return isProcessed;
//	}
//
//	public void setIsProcessed(String isProcessed) {
//		this.isProcessed = isProcessed;
//	}
//
//	@Override
//	public String toString() {
//		if (applicationType == APPLICATION_TYPE.BATCH_UPLOAD) {
//			return "Topics [id=" + id + ", picasaId=" + picasaId
//					+ ", imagePicasaUrl=" + imagePicasaUrl + ", imageUrl="
//					+ imageUrl + ", topicName=" + topicName + ", topicDescription="
//					+ topicDescription + ", topicParentId=" + topicParentId
//					+ ", isProcessed=" + isProcessed + "]";
//		} else {
//			return "insert into sos_topics(id, picasa_id, name, description, image_url, is_delete, created_dt, created_by, topicParent) "
//					+ "values ('"
//					+ id
//					+ "', '"
//					+ picasaId
//					+ "', '"
//					+ topicName
//					+ "', '"
//					+ topicDescription
//					+ "'', '"
//					+ imagePicasaUrl
//					+ "', 'N'"
//					+ "', current_time"
//					+ "', 'system'"
//					+ ", "
//					+ (topicParentId == null ? "null" : "'" + topicParentId
//							+ "'") + ");";
//		}
//	}
//
//}
