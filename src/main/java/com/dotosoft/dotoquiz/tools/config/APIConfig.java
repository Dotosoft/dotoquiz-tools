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

package com.dotosoft.dotoquiz.tools.config;

import static java.lang.String.format;

import com.dotosoft.dotoquiz.tools.config.metadata.AuthenticationServer;
import com.dotosoft.dotoquiz.tools.config.metadata.ClientSecret;

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
