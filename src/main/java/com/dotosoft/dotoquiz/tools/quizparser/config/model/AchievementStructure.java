package com.dotosoft.dotoquiz.tools.quizparser.config.model;

public class AchievementStructure {
	private String iAchievementId;
	private String iAlbumIdPicasa;
	private String iImageURLPicasa;
	private String iImageURL;
	private String iAchievementName;
	private String iAchievementDescription;
	private String iIsProcessed;

	public String getiAchievementId() {
		return iAchievementId;
	}

	public void setiAchievementId(String iAchievementId) {
		this.iAchievementId = iAchievementId;
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

	public String getiAchievementName() {
		return iAchievementName;
	}

	public void setiAchievementName(String iAchievementName) {
		this.iAchievementName = iAchievementName;
	}

	public String getiAchievementDescription() {
		return iAchievementDescription;
	}

	public void setiAchievementDescription(String iAchievementDescription) {
		this.iAchievementDescription = iAchievementDescription;
	}

	public String getiIsProcessed() {
		return iIsProcessed;
	}

	public void setiIsProcessed(String iIsProcessed) {
		this.iIsProcessed = iIsProcessed;
	}

	@Override
	public String toString() {
		return "AchievementStructure [iAchievementId=" + iAchievementId
				+ ", iAlbumIdPicasa=" + iAlbumIdPicasa + ", iImageURLPicasa="
				+ iImageURLPicasa + ", iImageURL=" + iImageURL
				+ ", iAchievementName=" + iAchievementName
				+ ", iAchievementDescription=" + iAchievementDescription
				+ ", iIsProcessed=" + iIsProcessed + "]";
	}

}
