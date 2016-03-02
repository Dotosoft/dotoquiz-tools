package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.utils.StringUtils;

public class PrintCommand implements Command {

	private String printMessage;
	private String printKey;

	public void setPrintMessage(String printMessage) {
		this.printMessage = printMessage;
	}

	public void setPrintKey(String printKey) {
		this.printKey = printKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		if(StringUtils.hasValue(printMessage)) {
			System.out.println(printMessage);
		}
		
		if(context.containsKey(printKey)) {
			System.out.println(context.get(printKey));
		}
		
		return false;
	}

}
