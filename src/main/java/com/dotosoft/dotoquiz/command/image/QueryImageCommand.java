package com.dotosoft.dotoquiz.command.image;

import java.util.List;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class QueryImageCommand implements Filter {

	private String authKey;
	private String imageClassName;
	private String resultKey;
	private boolean showAll;

	public void setResultKey(String resultKey) {
		this.resultKey = resultKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setImageClassName(String imageClassName) {
		this.imageClassName = imageClassName;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		Class imageClazz = Class.forName(imageClassName);

		ImageWebClient webClient;
		if (StringUtils.hasValue(authKey)) {
			Object credential = context.get(authKey);
			webClient = SingletonFactory.getInstance(imageClazz, credential);
		} else {
			webClient = SingletonFactory.getInstance(imageClazz, context);
		}

		List albumEntries = webClient.getAlbums(showAll);
		/* for (Object albumEntry : albumEntries) {
			System.out.println("album::: " + albumEntry);
		} */
		
		context.put(resultKey, albumEntries);

		return false;
	}

	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}

}
