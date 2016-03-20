package com.dotosoft.dotoquiz.command.generic;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.chain.Command;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;

public abstract class AbstractCommand implements Command {
	private StrSubstitutor sub;
	private Logger log;
	
	public AbstractCommand() {
		log = Logger.getLogger(getClass().getName());
	}
	
	public Object getProperty(Object data, String expression) {
		return getProperty(data, expression, null);
	}
	
	public Object getProperty(Object data, String expression, Object defaultValue) {
		try {
			Object returnValue = PropertyUtils.getProperty(data, expression);
			if(returnValue != null) {
				return returnValue;
			}
		} catch (Exception ex) {}
		
		return defaultValue;
	}
}