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
