package com.dotosoft.dotoquiz.command.datasheet;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

public class CreateDatasheetAPICommand implements Filter {

	private String dataClassName;
	private String authKey;
	private String dataKey;
	private String toKey;

	public void setDataClassName(String dataClassName) {
		this.dataClassName = dataClassName;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
//		Class dataClazz = Class.forName(dataClassName);
//
//		DatasheetClient webClient;
//		if (StringUtils.hasValue(authKey)) {
//			Object credential = BeanUtils.getProperty(context, authKey);
//			Object data = BeanUtils.getProperty(context, dataKey);
//			if (credential != null && data != null) {
//				webClient = SingletonFactory.getInstance(dataClazz, credential, data);
//			} else {
//				webClient = SingletonFactory.getInstance(dataClazz, context);
//			}
//		} else {
//			webClient = SingletonFactory.getInstance(dataClazz, context);
//		}
//
//		context.put(toKey, webClient);

		return false;
	}

	@Override
	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}

}
