package com.dotosoft.dotoquiz.command.loop;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class EndLoopCommand implements Command {

	private String keyCheck;
	private String keyCommand;

	public void setKeyCheck(String keyCheck) {
		this.keyCheck = keyCheck;
	}

	public void setKeyCommand(String keyCommand) {
		this.keyCommand = keyCommand;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		if (context.containsKey(keyCheck)) {
			Command command = (Command) context.get(keyCommand);
			command.execute(context);
		}
		return true;
	}

}
