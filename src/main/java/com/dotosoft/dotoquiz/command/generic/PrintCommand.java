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

package com.dotosoft.dotoquiz.command.generic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class PrintCommand implements Command {

	private static final Logger log = LogManager.getLogger(Command.class.getName());
	
	private String message;
	private String key;

	public void setMessage(String message) {
		this.message = message;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		String messageInfo = "";
		
		if (StringUtils.hasValue(message)) {
			messageInfo = message;
		}
		
		List paramMessages = new ArrayList();
		String[] splitKeys = key.split(",");
		for(String splitKey : splitKeys) {
			Object param = BeanUtils.getProperty(context, splitKey);
			paramMessages.add(param);
		}
		
		log.info ( String.format(messageInfo, paramMessages.toArray()) );

		return false;
	}

}
