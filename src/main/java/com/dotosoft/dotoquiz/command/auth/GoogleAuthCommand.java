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

package com.dotosoft.dotoquiz.command.auth;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.command.auth.impl.GoogleOAuth;
import com.dotosoft.dotoquiz.tools.config.DotoQuizContext;
import com.google.api.client.auth.oauth2.Credential;

public class GoogleAuthCommand implements Command {
	
	@Override
	public boolean execute(Context context) throws Exception {
		DotoQuizContext ctx = (DotoQuizContext) context;
		GoogleOAuth auth = new GoogleOAuth(ctx.getSettings());
		Credential credential = auth.authenticateOauth(ctx.getSettings().getApi().isAllowInteractive(), ctx.getSyncState());
		ctx.setCredential(credential);
		
		if(credential != null) {
			return false;
		}
		
		System.err.println("Authentication Failed!");
		return true;
	}
}
