package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class ElseIfCommand extends ChainBase {

	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";

	private String evaluate;

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = false;
		if (!IfCommand.getIfCommandKey()) {
			evaluate = evaluate.replaceAll("\\s+", "");
			String[] parts = evaluate.split(regexStr);
			result = evaluate(context, parts);
			if (result) {
				result = super.execute(context);
			}
		}

		return false;
	}

	private boolean evaluate(Context context, String[] parts) throws Exception {
		boolean result = false;
		Object obj1 = BeanUtils.getProperty(context, parts[0]);
		String op = parts[1];
		Object obj2 = BeanUtils.getProperty(context, parts[2]);

		switch (op) {
		case "!=":
			result = obj1 != obj2;
			break;
		case "==":
			result = obj1 == obj2;
			break;
		case "||":
			result = ((Boolean) obj1 || (Boolean) obj2);
			break;
		case "&&":
			result = ((Boolean) obj1 && (Boolean) obj2);
			break;
		default:
			throw new Exception("Expression is not valid");
		}
		return result;
	}
}
