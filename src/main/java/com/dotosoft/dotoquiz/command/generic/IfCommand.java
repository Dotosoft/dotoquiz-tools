package com.dotosoft.dotoquiz.command.generic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;
import org.hibernate.internal.util.compare.EqualsHelper;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class IfCommand extends ChainBase {

	private static final String regexStr = "(?=[!=&|][=&|])|(?<=[!=&|][=&|])";
	private static boolean ifFlag = true;
	
	private String evaluate;
	
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	
	public static boolean getIfCommandKey() {
		return IfCommand.ifFlag;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		IfCommand.ifFlag = true;
		evaluate = evaluate.replaceAll("\\s+", "");
		String[] parts = evaluate.split(regexStr);
		boolean result = evaluate(context, parts);

		if (result) {
			result = super.execute(context);
		} else {
			IfCommand.ifFlag = false;
		}

		return false;
	}

	private boolean evaluate(Context context, String[] parts) throws Exception {
		boolean result = false;
		Object obj1 = extractValue(context, parts[0]);
		String op = parts[1];
		Object obj2 = extractValue(context, parts[2]);

		switch (op) {
		case "!=":
			result = !(EqualsHelper.equals(obj1, obj2));
			break;
		case "==":
			result = (EqualsHelper.equals(obj1, obj2));
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
	
	Pattern pattern;
	public IfCommand() {
		pattern = Pattern.compile("([\"'])((?:(?=(\\\\?))\\3.)*?)\\1");
	}
	
	private Object extractValue(Context context, String part) {
		// "(["'])(?:(?=(\\?))\2.)*?\1"
		Object result = null;
		Matcher matcher = pattern.matcher(part);
		if(matcher.find()) {
			result = matcher.group(2);
		} else {
			result = BeanUtils.getProperty(context, part);
		}
		return result;
	}

}
