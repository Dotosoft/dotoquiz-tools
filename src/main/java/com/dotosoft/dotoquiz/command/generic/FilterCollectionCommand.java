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

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.collections.CollectionUtils;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class FilterCollectionCommand implements Command {

	private String fromKey;
	private String toKey;
	private String filterExpression;
	private String filterValue;

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		Collection dataCollection = (Collection) BeanUtils.getProperty(context, fromKey);
		BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate(
				filterExpression, filterValue);
		CollectionUtils.filter(dataCollection, predicate);
		
		if(dataCollection.isEmpty()) {
			context.remove(toKey);
		} else {
			context.put(toKey, dataCollection);
		}

		return false;
	}

}
