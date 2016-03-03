package com.dotosoft.dotoquiz.tools.util;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtils {
	
	public static Object getProperty(Object data, String expression) {
		try {
			return PropertyUtils.getProperty(data, expression);
		} catch (Exception ex) {}
		return null;
	}
	
}
