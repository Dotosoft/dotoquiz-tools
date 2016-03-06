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

package com.dotosoft.dotoquiz.command.datasheet;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class ShowColumnHeaderCommand implements Command {

	private String apiKey;
	private String dataKey;

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
//		DatasheetClient webClient = (DatasheetClient) BeanUtils.getProperty(context, apiKey);
//		if (webClient == null) {
//			throw new Exception("API is not exist!");
//		}
//
//		Object value = BeanUtils.getProperty(context, dataKey);
//		if(value != null) {
//			webClient.showColumnHeader(value);
//		}

		return false;
	}

}