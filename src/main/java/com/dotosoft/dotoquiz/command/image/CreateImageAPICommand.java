package com.dotosoft.dotoquiz.command.image;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class CreateImageAPICommand implements Filter {

	private String imageClassName;
	private String authKey;
	private String toKey;

	public void setImageClassName(String imageClassName) {
		this.imageClassName = imageClassName;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		Class imageClazz = Class.forName(imageClassName);

		ImageWebClient webClient;
		if (StringUtils.hasValue(authKey)) {
			Object credential = BeanUtils.getProperty(context, authKey);
			if (credential != null) {
				webClient = SingletonFactory.getInstance(imageClazz, credential);
			} else {
				webClient = SingletonFactory.getInstance(imageClazz, context);
			}
		} else {
			webClient = SingletonFactory.getInstance(imageClazz, context);
		}

		context.put(toKey, webClient);

		return false;
	}
	
	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}

}
