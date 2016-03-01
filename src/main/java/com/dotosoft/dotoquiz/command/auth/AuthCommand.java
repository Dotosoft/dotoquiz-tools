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

import com.dotosoft.dotoquiz.command.auth.impl.IAuth;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;

public class AuthCommand implements Command {
	
	private String key;
	private String authClassName;
	private String returnClassName;

	public void setKey(String key) {
		this.key = key;
	}

	public void setAuthClassName(String authClassName) {
		this.authClassName = authClassName;
	}

	public void setReturnClassName(String returnClassName) {
		this.returnClassName = returnClassName;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		Class authClazz = Class.forName(authClassName);
		IAuth authInstance = SingletonFactory.getInstance(authClazz, context);
		
		Object credential = authInstance.authenticate();
		if (credential != null) {
			Class<?> returnClazz = Class.forName(returnClassName);
			context.put(key, returnClazz.cast(credential));
			
			return false;
		}

		System.err.println("Authentication Failed!");
		return true;
	}
}
