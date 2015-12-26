package com.dotosoft.tools.quizparser;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.Builder;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.google.gdata.client.GoogleAuthTokenFactory.AuthSubToken;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.UserFeed;

public class DotoQuizGoogle {
	/**
	 * Be sure to specify the name of your application. If the application name
	 * is {@code null} or blank, the application will log a warning. Suggested
	 * format is "MyCompany-ProductName/1.0".
	 */
	private static final String APPLICATION_NAME = "dotoquiz";
	private static final int CONNECTION_TIMEOUT_SECS = 10;

	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/oauth2_sample");

	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** OAuth 2.0 scopes. */
	private static final List<String> SCOPES = Arrays.asList(
			"https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/userinfo.email",
			"https://picasaweb.google.com/data/");

	private static Oauth2 oauth2;
	private static GoogleClientSecrets clientSecrets;

	/** Authorizes the installed application to access user's protected data. */
	public static Credential authorize() throws Exception {
		// Setup Configurations
		if(httpTransport == null) {
			SetupConfiguration();
		}
		
		// load client secrets
		clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DotoQuizGoogle.class.getResourceAsStream("/client_secrets.json")));
					
		if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ " 
					+ "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}
		
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).build();

		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}
	
	private static void SetupConfiguration() {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static void tokenInfo(String accessToken) throws IOException {
		header("Validating a token");
		Tokeninfo tokeninfo = oauth2.tokeninfo().setAccessToken(accessToken).execute();
		System.out.println(tokeninfo.toPrettyString());
		if (!tokeninfo.getAudience().equals(clientSecrets.getDetails().getClientId())) {
			System.err.println("ERROR: audience does not match our client ID!");
		}
	}

	private static void userInfo() throws IOException {
		header("Obtaining User Profile Information");
		Userinfoplus userinfo = oauth2.userinfo().get().execute();
		System.out.println(userinfo.toPrettyString());
	}

	static void header(String name) {
		System.out.println();
		System.out.println("================== " + name + " ==================");
		System.out.println();
	}
	
	public static void main(String[] args) {
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
			
			SetupConfiguration();
			
			// authorization
			Credential credential = authorize();
			
//			GoogleCredential credential = new GoogleCredential.Builder()
//	            .setTransport(httpTransport)
//	            .setJsonFactory(JSON_FACTORY)
//	            .setServiceAccountId("dotosoft-images@dotoquiz.iam.gserviceaccount.com")
//	            .setServiceAccountPrivateKeyFromP12File(new File("/Users/denis/Documents/Dotosoft/SecretKey/DotoQuiz-1aa033e1165c.p12"))
//	            .setServiceAccountScopes(Collections.singleton("https://picasaweb.google.com/data/"))
//	            .build();
//			credential.getRefreshToken();
			
			
//			// set up global Oauth2 instance
//			oauth2 = new Oauth2.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
////			
////			// run commands
//			tokenInfo(credential.getAccessToken());
//			userInfo(); 
			
			PicasawebService myService = new PicasawebService("exampleCo-exampleApp-1");
			myService.setOAuth2Credentials( credential );
			myService.setConnectTimeout( 1000 * CONNECTION_TIMEOUT_SECS );
			myService.setReadTimeout(1000 * CONNECTION_TIMEOUT_SECS);
			// myService.setAuthSubToken(credential.getAccessToken());
			
			
			// String requestUrl = AuthSubUtil.getRequestUrl("http://dotosoft.com","https://picasaweb.google.com/data/",false,true);
//			myService.setAuthSubToken(credential.getAccessToken());
			
			// String sessionToken = AuthSubUtil.exchangeForSessionToken(onetimeUseToken, null);
			
//			PicasawebClient client = new PicasawebClient(myService, "dotosoft.images@gmail.com", "user2010");
//			for(AlbumEntry entry : client.getAlbums()) {
//				System.out.println(":::: " + entry);
//			}
			
			// myService.setUserToken(credential.getAccessToken());
			// myService.setUserCredentials("dotosoft.images@gmail.com", "User2010");
			
//			myService.getAuthTokenFactory().
			
//			URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/default?kind=album");
//
//			UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);
//
//			for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
//			    System.out.println(myAlbum.getTitle().getPlainText());
//			}
			
			URL feedURL = new URL("https://picasaweb.google.com/data/feed/api/user/default");
			AlbumEntry myAlbum = new AlbumEntry();

			myAlbum.setTitle(new PlainTextConstruct("Trip to France"));
			myAlbum.setDescription(new PlainTextConstruct("My recent trip to France was delightful!"));

			AlbumEntry insertedEntry = myService.insert(feedURL, myAlbum);
			
			// success!
			return;
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		System.exit(1);
	}
}
