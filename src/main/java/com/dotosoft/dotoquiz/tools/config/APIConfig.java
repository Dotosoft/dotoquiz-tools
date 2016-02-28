package com.dotosoft.dotoquiz.tools.config;

import static java.lang.String.format;

import com.dotosoft.dotoquiz.tools.config.model.AuthenticationServer;
import com.dotosoft.dotoquiz.tools.config.model.ClientSecret;

public class APIConfig {
	private ClientSecret clientSecret;
	private AuthenticationServer authenticationServer;
	private String refreshToken;
	private String dataStoreDir;
	private boolean allowInteractive = false;

	public boolean isAllowInteractive() {
		return allowInteractive;
	}

	public void setAllowInteractive(boolean allowInteractive) {
		this.allowInteractive = allowInteractive;
	}

	public ClientSecret getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(ClientSecret clientSecret) {
		this.clientSecret = clientSecret;
	}

	public AuthenticationServer getAuthenticationServer() {
		return authenticationServer;
	}

	public void setAuthenticationServer(
			AuthenticationServer authenticationServer) {
		this.authenticationServer = authenticationServer;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getDataStoreDir() {
		return dataStoreDir;
	}

	public void setDataStoreDir(String dataStoreDir) {
		this.dataStoreDir = dataStoreDir;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("API Configurations:\n")
			.append(format("clientSecret: %s\n", clientSecret))
			.append(format("authenticationServer: %s\n", authenticationServer))
			.append(format("refreshToken: %s\n", refreshToken))
			.append(format("dataStoreDir: %s\n", dataStoreDir))
			.append(format("allowInteractive: %s\n", allowInteractive))
			.toString();
	}

}
