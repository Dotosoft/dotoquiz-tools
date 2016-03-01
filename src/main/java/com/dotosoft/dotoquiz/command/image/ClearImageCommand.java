/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */

package com.dotosoft.dotoquiz.command.image;

import java.util.List;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.Filter;

import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class ClearImageCommand implements Filter {

	private String authKey;
	private String imageClassName;
	private String imageKey;
	private String deletePhoto;

	public void setImageKey(String imageKey) {
		this.imageKey = imageKey;
	}

	public void setDeletePhoto(String deletePhoto) {
		this.deletePhoto = deletePhoto;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public void setImageClassName(String imageClassName) {
		this.imageClassName = imageClassName;
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

		List photoCollection = (List) context.get(imageKey);
		if(StringUtils.hasValue(deletePhoto)) {
			photoCollection = webClient.filterPhoto(photoCollection, deletePhoto);
		}
		
		for (Object photo : photoCollection) {
			webClient.deletePhoto(photo);
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