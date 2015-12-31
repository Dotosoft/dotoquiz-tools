package com.dotosoft.dotoquiz.common;

public class QuizParserConstant {
	public enum APPLICATION_TYPE {
		CLEAR, DB, SYNC
	}

	public enum DATA_TYPE {
		EXCEL, GOOGLESHEET
	}

	public enum IMAGE_HOSTING_TYPE {
		PICASA
	}

	public static final String SCOPE_PICASA = "https://picasaweb.google.com/data/";
	public static final String SCOPE_GOOGLESHEET = "https://spreadsheets.google.com/feeds";

	// Constant Service
	public static final String SYNC_CLIENT_NAME = "com.dotosoft.quizparser";
	public static final int CONNECTION_TIMEOUT_SECS = 10;

	// LocalServer Oauth Parameter
	public static final String LISTEN_OAUTH_IP = "127.0.0.1";
	public static final int LISTEN_OAUTH_PORT = 8080;

	// Setting Constant
	public static final String REFRESH_TOKEN = "RefreshToken";
	public static final String APP_TYPE = "ApplicationType";
	public static final String SYNC_FILE = "SyncFile";
	public static final String DATA_TYPE = "DataType";
	public static final String IMAGE_HOSTING_TYPE = "ImageHostingType";
}
