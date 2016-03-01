package com.dotosoft.dotoquiz.command.loop;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class StartLoopCommand implements Command {

	private String keyCommand;

	public void setKeyCommand(String keyCommand) {
		this.keyCommand = keyCommand;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		context.put(keyCommand, this);
		return false;
	}

}
