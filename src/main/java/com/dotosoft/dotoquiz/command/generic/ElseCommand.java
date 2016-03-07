package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;

public class ElseCommand extends ChainBase {
	
	@Override
	public boolean execute(Context context) throws Exception {
		boolean result = false;
		if(!IfCommand.getIfCommandKey()) {
			result = super.execute(context);
		}
		return result;
	}
	
}
