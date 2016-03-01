package com.dotosoft.dotoquiz.command.loop;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

public class EndLoopCommand implements Filter {

	@Override
	public boolean execute(Context context) throws Exception {
		return false;
	}

	@Override
	public boolean postprocess(Context context, Exception exception) {
		return false;
	}

}
