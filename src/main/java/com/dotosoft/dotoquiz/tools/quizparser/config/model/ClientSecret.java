package com.dotosoft.dotoquiz.tools.quizparser.config.model;

import java.util.List;

public class ClientSecret {
	private String clientId;
	private String projectId;
	private String authURI;
	private String tokenURI;
	private String authProvider;
	private String clientSecret;
	private List<String> redirectURI;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getAuthURI() {
		return authURI;
	}

	public void setAuthURI(String authURI) {
		this.authURI = authURI;
	}

	public String getTokenURI() {
		return tokenURI;
	}

	public void setTokenURI(String tokenURI) {
		this.tokenURI = tokenURI;
	}

	public String getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(String authProvider) {
		this.authProvider = authProvider;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public List<String> getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(List<String> redirectURI) {
		this.redirectURI = redirectURI;
	}

	@Override
	public String toString() {
		return "ClientSecret [clientId=" + clientId + ", projectId="
				+ projectId + ", authURI=" + authURI + ", tokenURI=" + tokenURI
				+ ", authProvider=" + authProvider + ", clientSecret="
				+ clientSecret + ", redirectURI=" + redirectURI + "]";
	}

}
