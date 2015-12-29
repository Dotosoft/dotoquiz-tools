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

package com.dotosoft.tools.quizparser.auth;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

import com.dotosoft.tools.quizparser.DotoQuizGoogle;
import com.dotosoft.tools.quizparser.config.QuizParserConstant;
import com.dotosoft.tools.quizparser.config.Settings;
import com.dotosoft.tools.quizparser.data.GooglesheetClient;
import com.dotosoft.tools.quizparser.images.PicasawebClient;
import com.dotosoft.tools.quizparser.utils.SyncState;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.gdata.util.ServiceException;

/**
 * Utility class to authenticate using Oauth 2.0.
 *
 * Displays a browser window if no credentials are available in the
 * prefs storage. Once the user has logged in, stores the refresh
 * token in prefs for next time.
 */
public class GoogleOAuth {
    private static final Logger log = Logger.getLogger(GoogleOAuth.class);
    
    private static final Object lock = new Object();
    private static final Dimension frameSize = new Dimension( 650, 500 );
    private static volatile String token;
    private static final String SUCCESS_CODE = "Success code=";

    private static GoogleClientSecrets clientSecrets;
    /** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File( System.getProperty("user.home"), ".store/oauth2_dotoquiz" );
	
	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;
	
	/** OAuth 2.0 scopes. */
	private static final List<String> SCOPES = Arrays.asList(
			QuizParserConstant.SCOPE_PICASA, 
			QuizParserConstant.SCOPE_GOOGLESHEET);

    public GoogleOAuth()
    {
    	try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
    }

    public PicasawebClient authenticatePicasa( Settings settings, boolean allowInteractive, SyncState state ) throws IOException, GeneralSecurityException {
    	Credential cred = authenticateOauth(settings, allowInteractive, state);

        if( cred != null ){

            log.info("Building PicasaWeb Client...");

            // Build a web client using the credentials we created
            return new PicasawebClient( cred );
        }

        return null;
    }
    
    public GooglesheetClient authenticateGooglesheet(String googlesheetFileName, Settings settings, boolean allowInteractive, SyncState state) throws IOException, GeneralSecurityException, IOException, ServiceException  {
    	Credential cred = authenticateOauth(settings, allowInteractive, state);

        if( cred != null ){

            log.info("Building Googlesheet Client...");

            // Build a web client using the credentials we created
            return new GooglesheetClient( cred , googlesheetFileName);
        }

        return null;
    }
    
    private Credential authenticateOauth( Settings settings, boolean allowInteractive, SyncState state )  throws IOException, GeneralSecurityException {
    	log.info("Preparing to authenticate via OAuth...");
        Credential cred = null;

        String refreshToken = settings.getRefreshToken();
        if( refreshToken != null )
        {
            // We have a refresh token - so get some refreshed credentials
            cred = getRefreshedCredentials( refreshToken );
        }

        if( cred == null ) {

            // Either there was no valid refresh token, or the credentials could not
            // be created (they may have been revoked). So run the auth flow
            log.info("No credentials - beginning OAuth flow...");

            state.setStatus( "Requesting Google Authentication...");
            
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DotoQuizGoogle.class.getResourceAsStream("/client_secrets.json")));
            if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
    			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ " 
    					+ "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
    			System.exit(1);
    		}
    		
    		// set up authorization code flow
    		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(dataStoreFactory).build();
    		
    		if(allowInteractive) {
    			String redirectUrl = clientSecrets.getDetails().getRedirectUris().get(0);
    			String authorizationUrl = flow.newAuthorizationUrl()
                        .setRedirectUri(redirectUrl)
                        .setAccessType("offline")
                        .setApprovalPrompt("force")
                        .build();

			    // Display the interactive GUI for the user to log in via the browser
			    String code = initAndShowGUI( authorizationUrl, state );
			
			    log.info("Token received from UI. Requesting credentials...");
			
			    // Now we have the code from the interactive login, set up the
			    // credentials request and call it.
			    GoogleTokenResponse response = flow.newTokenRequest(code)
			    	.setRedirectUri(redirectUrl).execute();
			
			    // Retrieve the credential from the request response
			    cred = new GoogleCredential.Builder().setTransport(httpTransport)
			    	.setJsonFactory(JSON_FACTORY).setClientSecrets(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret())
			    	.build().setFromTokenResponse(response);
			
    		} else {
	            // Retrieve the credential from the request response
	    		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(8080).build();
	    		cred = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    		}
    		
    		state.setStatus( "Google Authentication succeeded.");
            log.info("Credentials received - storing refresh token...");

            // Squirrel this away for next time
            settings.setRefreshToken( cred.getRefreshToken() );
            settings.saveSettings();
        }
        
        return cred;
    }

    public Credential getRefreshedCredentials(String refreshCode) throws IOException, GeneralSecurityException {
        // HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        log.info("Getting access token for refresh token..");

        try {
        	clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DotoQuizGoogle.class.getResourceAsStream("/client_secrets.json")));
            if (clientSecrets.getDetails().getClientId().startsWith("Enter") || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
    			System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ " 
    					+ "into oauth2-cmdline-sample/src/main/resources/client_secrets.json");
    			System.exit(1);
    		}
            
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    httpTransport, JSON_FACTORY, refreshCode, clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret() )
                    .execute();

            return new GoogleCredential().setAccessToken(response.getAccessToken());

        }
        catch( UnknownHostException ex ){
            log.error( "Unknown host. No web access?");
            throw ex;
        }
        catch (IOException e) {
            log.error( "Exception getting refreshed auth: ", e );
        }
        return null;
    }
    
    
    // build UI
    private static String initAndShowGUI(final String url, final SyncState state ) {

        log.info("Displaying OAuth Login frame...");

        final JFrame frame = new JFrame("Authenticate Picasa");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.getContentPane().setLayout(null); // do the layout manually

        final JFXPanel fxPanel = new JFXPanel();

        frame.add(fxPanel);
        frame.setVisible(true);

        fxPanel.setSize(frameSize);
        fxPanel.setLocation(0,0);

        frame.getContentPane().setPreferredSize(frameSize);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - fxPanel.getSize().width / 2, screenSize.height / 2 - fxPanel.getSize().height / 2);

        frame.pack();
        frame.setResizable(false);

        String authToken = "";

        try {
            Platform.runLater(new Runnable() {
                public void run() {
                    log.info( "Initialising login frame on background thread.");
                    synchronized ( lock ){
                        initWebView(fxPanel, frame, url, state );
                    }
                }
            });

            synchronized ( lock ) {
                lock.wait();

                log.info( "User closed window.");

                authToken = token;
            }
        }
        catch( Exception ex ){
            log.error("Unexpected exception opening interactive login screen.");
        }

        return authToken;
    }

    /* Creates a WebView and fires up google.com */
    private static void initWebView(final JFXPanel fxPanel, final JFrame frame, String url, final SyncState state ) {
        log.info( "Initialising WebView on GUI thread...");

        Group group = new Group();
        Scene scene = new Scene(group);
        fxPanel.setScene(scene);

        WebView webView = new WebView();

        group.getChildren().add(webView);
        webView.setMinSize(frameSize.width, frameSize.height);
        webView.setMaxSize(frameSize.width, frameSize.height);
        webView.setZoom( 0.80 );

        // Obtain the webEngine to navigate
        final WebEngine webEngine = webView.getEngine();

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
                    public void changed(ObservableValue ov, State oldState, State newState) {

                        HandleWebTitleChange( webEngine, frame, newState, state );
                    }
                });

        webEngine.load(url);
    }

    private static void HandleWebTitleChange( WebEngine webEngine, JFrame frame, State newState, SyncState state )
    {
        if (newState == Worker.State.SUCCEEDED) {
            log.info("Page refreshed: " + webEngine.getTitle());

            frame.setTitle(webEngine.getTitle());

            if( webEngine.getTitle().startsWith( SUCCESS_CODE ) ) {

                synchronized ( lock ) {
                    token = webEngine.getTitle().substring( SUCCESS_CODE.length() );
                    state.setStatus( "Login successful.");
                    lock.notify();
                }

                log.info("Hiding login panel.");

                frame.setVisible( false );
            }
        }
        else if( newState == Worker.State.FAILED ) {
            log.error("Error loading Google Auth Page!");
            state.setStatus( "Unable to load Google Authentication page.");
            state.cancel( true );
            frame.setVisible(false);
        }
    }
}
