package com.dotosoft.dotoquiz.command.image;

import org.apache.commons.chain.Filter;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class AddAlbumCommand implements Filter {

	private String imageClassName;
	private String authKey;
	private String title;
	private String description;
	private String checkKey;
	private boolean isPublic;

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	public void setImageClassName(String imageClassName) {
		this.imageClassName = imageClassName;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
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

		boolean isInsert = true;
		if(StringUtils.hasValue(checkKey)) {
			if(BeanUtils.getProperty(context, checkKey) == null) {
				return false;
			}
		}
		
		if(isInsert) {
			webClient.insertAlbum(title, description, isPublic);
		}

		return false;
	}

	public boolean postprocess(Context context, Exception exception) {
		if (exception == null) return false;
		exception.printStackTrace();
		System.err.println("Exception " + exception.getMessage() + " occurred.");
		return true;
	}
}