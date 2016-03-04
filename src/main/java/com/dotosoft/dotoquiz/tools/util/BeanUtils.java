package com.dotosoft.dotoquiz.tools.util;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {
	
	public static Object getProperty(Object data, String expression) {
		return getProperty(data, expression, null);
	}
	
	public static Object getProperty(Object data, String expression, Object defaultValue) {
		try {
			Object returnValue = PropertyUtils.getProperty(data, expression);
			if(returnValue != null) {
				return returnValue;
			}
		} catch (Exception ex) {}
		
		return defaultValue;
	}
	
}
