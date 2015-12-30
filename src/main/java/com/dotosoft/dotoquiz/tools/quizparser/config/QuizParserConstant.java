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

package com.dotosoft.dotoquiz.tools.quizparser.config;

public class QuizParserConstant {
	// Application Type Definition
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
	
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String SYSTEM_USER = "system";
}
