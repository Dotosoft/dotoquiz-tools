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
