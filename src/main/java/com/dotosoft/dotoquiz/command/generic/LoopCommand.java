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

package com.dotosoft.dotoquiz.command.generic;

import java.util.Collection;

import org.apache.commons.chain.Context;
import org.apache.commons.chain.impl.ChainBase;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class LoopCommand extends ChainBase {

	private boolean doWhile = false;
	private String checkKey;
	private String checkCollectionKey;
	private int loopTime = 0;
	private String indexKey;

	public void setCheckCollectionKey(String checkCollectionKey) {
		this.checkCollectionKey = checkCollectionKey;
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}

	public void setDoWhile(boolean doWhile) {
		this.doWhile = doWhile;
	}

	public void setLoopTime(int loopTime) {
		this.loopTime = loopTime;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		
		if(StringUtils.hasValue(checkCollectionKey)) {
			Collection collection = (Collection) BeanUtils.getProperty(context, checkCollectionKey);
			loopTime = collection.size();
		}
		
		boolean result = false;
		Integer index = (Integer) BeanUtils.getProperty(context, indexKey, 0);
		context.put(indexKey, index);
		
		int loopTimeCheck = loopTime;
		
		boolean isLoopTime = (loopTimeCheck > 0);
		if (doWhile) {
			result = super.execute(context);
			if(isLoopTime) loopTimeCheck -= 1;
			context.put(indexKey, ++index);
		}
		while((isLoopTime && loopTimeCheck > 0) || (StringUtils.hasValue(checkKey) && BeanUtils.getProperty(context, checkKey) != null)) {
			result = super.execute(context);
			if (isLoopTime) loopTimeCheck -= 1;
			if (result) break;
			context.put(indexKey, ++index);
		}
		
		context.remove(indexKey);
		
		return result;
	}

}
