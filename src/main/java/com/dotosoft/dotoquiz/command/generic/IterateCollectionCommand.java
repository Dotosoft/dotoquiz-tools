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

import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class IterateCollectionCommand implements Command {
	private String collectionKey;
	private String incrementKey;
	private String toKey;

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	public void setIncrementKey(String incrementKey) {
		this.incrementKey = incrementKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		List coll = (List) BeanUtils.getProperty(context, collectionKey); 
		if(coll != null && !coll.isEmpty()) {
			Integer index = (Integer) context.get(incrementKey);
			if(index == null) {
				index = 0;
			}
			
			if(index >= coll.size()) {
				context.remove(toKey);
				context.remove(incrementKey);
			} else {
				context.put(toKey, coll.get(index));
				context.put(incrementKey, ++index);
			}
		}
		return false;
	}

}
