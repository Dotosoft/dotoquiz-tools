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

package com.dotosoft.dotoquiz.tools.metadata;

public class TopicStructure {
	private String iTopicId;
	private String iAlbumIdPicasa;
	private String iImageURLPicasa;
	private String iImageURL;
	private String iTopicName;
	private String iTopicDescription;
	private String iTopicParent;
	private String iIsProcessed;

	public String getiTopicId() {
		return iTopicId;
	}

	public void setiTopicId(String iTopicId) {
		this.iTopicId = iTopicId;
	}

	public String getiAlbumIdPicasa() {
		return iAlbumIdPicasa;
	}

	public void setiAlbumIdPicasa(String iAlbumIdPicasa) {
		this.iAlbumIdPicasa = iAlbumIdPicasa;
	}

	public String getiImageURLPicasa() {
		return iImageURLPicasa;
	}

	public void setiImageURLPicasa(String iImageURLPicasa) {
		this.iImageURLPicasa = iImageURLPicasa;
	}

	public String getiImageURL() {
		return iImageURL;
	}

	public void setiImageURL(String iImageURL) {
		this.iImageURL = iImageURL;
	}

	public String getiTopicName() {
		return iTopicName;
	}

	public void setiTopicName(String iTopicName) {
		this.iTopicName = iTopicName;
	}

	public String getiTopicDescription() {
		return iTopicDescription;
	}

	public void setiTopicDescription(String iTopicDescription) {
		this.iTopicDescription = iTopicDescription;
	}

	public String getiTopicParent() {
		return iTopicParent;
	}

	public void setiTopicParent(String iTopicParent) {
		this.iTopicParent = iTopicParent;
	}

	public String getiIsProcessed() {
		return iIsProcessed;
	}

	public void setiIsProcessed(String iIsProcessed) {
		this.iIsProcessed = iIsProcessed;
	}

	@Override
	public String toString() {
		return "TopicStructure [iTopicId=" + iTopicId + ", iAlbumIdPicasa="
				+ iAlbumIdPicasa + ", iImageURLPicasa=" + iImageURLPicasa
				+ ", iImageURL=" + iImageURL + ", iTopicName=" + iTopicName
				+ ", iTopicDescription=" + iTopicDescription
				+ ", iTopicParent=" + iTopicParent + ", iIsProcessed="
				+ iIsProcessed + "]";
	}

}
