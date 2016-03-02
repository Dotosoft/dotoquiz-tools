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
