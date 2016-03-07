package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class ArithmeticCommand implements Command {

	private static final String regexStr = "(?=[-/*+])|(?<=[-/*+])";
	
	private String evaluate;
	private String toKey;

	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		evaluate = evaluate.replaceAll("\\s+", "");
		String[] parts = evaluate.split(regexStr);
		double result = calculate(parts);
		context.put(toKey, result);
		
		return false;
	}
	
	private double calculate(String[] parts) {
		double result = Double.parseDouble(parts[0]);

		for (int i = 1; i < parts.length; i += 2) {
		    String op = parts[i];
		    double val = Double.parseDouble(parts[i+1]);
		    switch (op) {
		        case "*" :
		            result *= val;
		            break;
		        case "/" :
		            result /= val;
		            break;
		        case "+" :
		            result += val;
		            break;
		        case "-" :
		            result -= val;
		            break;
		    }
		}
		return result;
	}

}
