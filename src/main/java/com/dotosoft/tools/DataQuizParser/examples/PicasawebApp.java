package com.dotosoft.tools.DataQuizParser.examples;

import java.net.URL;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.AuthenticationException;

public class PicasawebApp {
	public static void main(String[] args) {
		try {
			PicasawebService myService = new PicasawebService("dotoquiz");
			myService.setUserCredentials("myemail@gmail.com", "xxxxxx");

			String requestUrl = AuthSubUtil.getRequestUrl("http://www.example.com/RetrieveToken",
					"https://picasaweb.google.com/data/", false, true);

			// String sessionToken =
			// AuthSubUtil.exchangeForSessionToken(onetimeUseToken, null);
			// PicasawebService.setAuthSubToken(sessionToken, null);

			URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/username?kind=album");

			UserFeed myUserFeed = myService.getFeed(feedUrl, UserFeed.class);

			for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
				System.out.println(myAlbum.getTitle().getPlainText());
			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}

//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.security.GeneralSecurityException;
//import java.util.Arrays;
//import java.util.List;
//
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.gdata.client.photos.PicasawebService;
//import com.google.gdata.data.PlainTextConstruct;
//import com.google.gdata.data.media.MediaFileSource;
//import com.google.gdata.data.photos.AlbumEntry;
//import com.google.gdata.data.photos.AlbumFeed;
//import com.google.gdata.data.photos.PhotoEntry;
//import com.google.gdata.data.photos.UserFeed;
//import com.google.gdata.util.ServiceException;
//
//public class App {
//	public static void main(String[] args)
//			throws MalformedURLException, GeneralSecurityException, IOException, ServiceException {
//
//		URL postUrl = new URL("https://picasaweb.google.com/data/feed/api/user/<UID>?&v=2");
//
//		File p12 = new File("PhotoGallery.p12");
//
//		HttpTransport httpTransport = new NetHttpTransport();
//		JacksonFactory jsonFactory = new JacksonFactory();
//		String[] SCOPESArray = { "https://picasaweb.google.com/data", "http://picasaweb.google.com/data/feed/api" };
//		final List SCOPES = Arrays.asList(SCOPESArray);
//		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
//				.setJsonFactory(jsonFactory).setServiceAccountId("<serviceaccountidfromdevconsole>")
//				.setServiceAccountScopes(SCOPES).setServiceAccountPrivateKeyFromP12File(p12).build();
//
//		credential.refreshToken();
//
//		PicasawebService myService = new PicasawebService("<UID>");
//		myService.setOAuth2Credentials(credential);
//		System.out.println("Token :" + credential.getAccessToken());
//
//		UserFeed myUserFeed = myService.getFeed(new URL(postUrl + "&access_token=" + credential.getAccessToken()),
//				UserFeed.class);
//
//		for (AlbumEntry myAlbum : myUserFeed.getAlbumEntries()) {
//			System.out.println("Album Name :" + myAlbum.getTitle().getPlainText());
//			System.out.println("Access:" + myAlbum.getAccess());
//			System.out.println("Id :" + myAlbum.getId());
//			System.out.println("XML :" + myAlbum.getXmlBlob());
//		}
//
//		credential.refreshToken();
//
//		/* Adding new album */
//
//		PicasawebService addService = new PicasawebService("<UID>");
//		addService.setOAuth2Credentials(credential);
//
//		AlbumEntry myAlbum = new AlbumEntry();
//
//		myAlbum.setTitle(new PlainTextConstruct("My Album"));
//		myAlbum.setDescription(new PlainTextConstruct("My trip to delhi was delightful!"));
//
//		AlbumEntry insertedEntry = addService.insert(postUrl, myAlbum);
//
//		URL albumPostUrl = new URL("https://picasaweb.google.com/data/feed/api/user/<UID>/albumid/<albumID>");
//
//		PhotoEntry myPhoto = new PhotoEntry();
//		myPhoto.setTitle(new PlainTextConstruct("Puppies FTW"));
//		myPhoto.setDescription(new PlainTextConstruct("Puppies are the greatest."));
//		myPhoto.setClient("myClientName");
//
//		MediaFileSource myMedia = new MediaFileSource(new File("puppies.jpg"), "image/jpeg");
//		myPhoto.setMediaSource(myMedia);
//
//		PhotoEntry returnedPhoto = myService.insert(albumPostUrl, myPhoto);
//	}
//
//}

//public class App {
//
//	public static String upload(String fullpathToImage) {
//		// The url of the image
//		String resultingURL = new String();
//
//		try {
//
//			// These are not needed
//			String GOOGLE_APP_NAME = "TralhasVariasScanPoster";
//
//			String GOOGLE_REFRESH_TOKEN = "";
//			String GOOGLE_ACCESS_TOKEN = "";
//
//			HttpTransport httpTransport = new NetHttpTransport();
//			JsonFactory jsonFactory = new JacksonFactory();
//			TokenResponse tokenResponse = new TokenResponse();
//
//			// Check of we have a previous Refresh Token cached
//			if (Config.GOOGLE_OAUTH_REFRESH_TOKEN.length() == 0) {
//				// No Google OAuth2 Key has been previously cached
//
//				// Request the user to grant access to the Picasa Resource (uses
//				// the Google Authentication servers)
//				AuthorizationCodeFlow.Builder codeFlowBuilder = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
//						jsonFactory, Config.GOOGLE_CLIENT_ID, Config.GOOGLE_CLIENT_SECRET,
//						Arrays.asList(Config.GOOGLE_SCOPE_PICASA));
//
//				AuthorizationCodeFlow codeFlow = codeFlowBuilder.build();
//				AuthorizationCodeRequestUrl authorizationUrl = codeFlow.newAuthorizationUrl();
//				authorizationUrl.setRedirectUri(Config.GOOGLE_REDIRECT_URI);
//
//				System.out.println("Go to the following address:\n" + authorizationUrl);
//				System.out.println("What is the 'code' url parameter?");
//				String code = new Scanner(System.in).nextLine();
//
//				// Use the code returned by the Google Authentication Server to
//				// generate an Access Code
//				AuthorizationCodeTokenRequest tokenRequest = codeFlow.newTokenRequest(code);
//				tokenRequest.setRedirectUri(Config.GOOGLE_REDIRECT_URI);
//				tokenResponse = tokenRequest.execute();
//
//				GOOGLE_REFRESH_TOKEN = tokenResponse.getRefreshToken();
//				GOOGLE_ACCESS_TOKEN = tokenResponse.getAccessToken();
//
//				// Store the Refresh Token for later usage (this avoid having to
//				// request the user to
//				// Grant access to the application via the webbrowser again
//				// saveTextFile(Config.GOOGLE_OAUTH_REFRESH_TOKEN_FILE, GOOGLE_REFRESH_TOKEN);
//			} else {
//				// There is a Google OAuth2 Key cached previously.
//				// Use the refresh token to get a new Access Token
//
//				// Get the cached Refresh Token
//				GOOGLE_REFRESH_TOKEN = new String(Config.GOOGLE_OAUTH_REFRESH_TOKEN);
//
//				// Now we need to get a new Access Token using our previously
//				// cached Refresh Token
//				RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(httpTransport, jsonFactory,
//						new GenericUrl(Config.GOOGLE_TOKEN_SERVER_URL), GOOGLE_REFRESH_TOKEN);
//
//				refreshTokenRequest.setClientAuthentication(
//						new BasicAuthentication(Config.GOOGLE_CLIENT_ID, Config.GOOGLE_CLIENT_SECRET));
//				refreshTokenRequest.setScopes(Arrays.asList(Config.GOOGLE_SCOPE_PICASA));
//
//				tokenResponse = refreshTokenRequest.execute();
//
//				// Get and set the Refresn the Access Tokens
//				GOOGLE_ACCESS_TOKEN = new String(tokenResponse.getAccessToken());
//			}
//
//			// At this point we have a valid Google Access Token
//			// Let us access Picasa then!
//			GoogleCredential credential = new GoogleCredential.Builder()
//					.setClientSecrets(Config.GOOGLE_CLIENT_ID, Config.GOOGLE_CLIENT_SECRET).setJsonFactory(jsonFactory)
//					.setTransport(httpTransport).build().setAccessToken(GOOGLE_ACCESS_TOKEN)
//					.setRefreshToken(GOOGLE_REFRESH_TOKEN);
//
//			PicasawebService picasaWebSvc = new PicasawebService("GOOGLE_APP_NAME");
//			picasaWebSvc.setOAuth2Credentials(credential);
//
//			URL feedUrl = new URL("https://picasaweb.google.com/data/feed/api/user/" + Config.PICASAWEB_LOGIN
//					+ "/albumid/" + Config.PICASAWEB_ALBUM_ID);
//
//			MediaFileSource myMedia = new MediaFileSource(new File(fullpathToImage), "image/jpeg");
//			PhotoEntry returnedPhoto = picasaWebSvc.insert(feedUrl, PhotoEntry.class, myMedia);
//
//			resultingURL = returnedPhoto.getMediaContents().get(0).getUrl();
//
//			if (resultingURL.toLowerCase().contains("please check your firewall")
//					|| resultingURL.toLowerCase().contains("error")) {
//				throw new Exception("The Windows Firewall seems to be blocking the upload...");
//			}
//
//			System.out.println("...DONE!");
//			System.out.println("Cover page URL: " + resultingURL);
//		} catch (Exception e) {
//			System.err.println("[BloggerImageUploader.java]: There was an error: " + e.getMessage());
//			return "";
//		}
//
//		// The result
//		return resultingURL;
//	}
//}