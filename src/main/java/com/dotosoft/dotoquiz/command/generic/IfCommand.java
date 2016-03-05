package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class IfCommand implements Command {

	private String test;
	
	@Override
	public boolean execute(Context context) throws Exception {
		
		return false;
	}

}
