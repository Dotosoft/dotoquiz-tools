package com.dotosoft.tools.quizparser.auth;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.dotosoft.tools.quizparser.DotoQuizGoogle;
import com.dotosoft.tools.quizparser.config.QuizParserConstant;
import com.dotosoft.tools.quizparser.config.Settings;
import com.dotosoft.tools.quizparser.images.PicasawebClient;
import com.dotosoft.tools.quizparser.images.syncutil.SyncState;
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

/**
 * Utility class to authenticate using Oauth 2.0.
 *
 * Displays a browser window if no credentials are available in the
 * prefs storage. Once the user has logged in, stores the refresh
 * token in prefs for next time.
 */
public class GoogleOAuth {
    private static final Logger log = Logger.getLogger(GoogleOAuth.class);

    private static GoogleClientSecrets clientSecrets;
    /** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	/**
	 * Global instance of the {@link DataStoreFactory}. The best practice is to
	 * make it a single globally shared instance across your application.
	 */
	private static FileDataStoreFactory dataStoreFactory;

	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;
	
	/** Directory to store user credentials. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".store/oauth2_dotoquiz");
	
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
        // final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        log.info("Preparing to authenticate via OAuth...");
        Credential cred = null;

        String refreshToken = settings.getRefreshToken();
        if( refreshToken != null )
        {
            // We have a refresh token - so get some refreshed credentials
            cred = getRefreshedCredentials( refreshToken );
        }

        if( cred == null && allowInteractive ) {

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
    		
            // Retrieve the credential from the request response
    		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setHost("127.0.0.1").setPort(8080).build();
    		cred = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            state.setStatus( "Google Authentication succeeded.");

            log.info("Credentials received - storing refresh token...");

            // Squirrel this away for next time
            settings.setRefreshToken( cred.getRefreshToken() );
            settings.saveSettings();
        }

        if( cred != null ){

            log.info("Building PicasaWeb Client...");

            // Build a web client using the credentials we created
            return new PicasawebClient( cred );
        }

        return null;
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
}
