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

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.generic.LookupCommand;

public class LoopCommand extends LookupCommand {

	public String checkKey;
	public int loopTime = 0;

	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		Command command = getCommand(context);
		if (command != null) {
			boolean result = false;
			if (loopTime > 0) {
				for (int i = 0; i < loopTime; i++) {
					result = command.execute(context);
					if (result)
						break;
				}
			} else {
				while (context.containsKey(checkKey)) {
					result = command.execute(context);
					if (result)
						break;
				}
			}
			return result;
		} else {
			return false;
		}

	}

}
