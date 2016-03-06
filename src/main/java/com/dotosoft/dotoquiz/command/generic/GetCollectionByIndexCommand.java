package com.dotosoft.dotoquiz.command.generic;

import java.util.Collection;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class GetCollectionByIndexCommand implements Command {

	private String collectionKey;
	private String indexKey;
	private String toKey;

	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	public boolean execute(Context context) throws Exception {

		Collection collection = (Collection) BeanUtils.getProperty(context, collectionKey);
		
		Object index = BeanUtils.getProperty(context, indexKey);
		Integer indexCollection;
		if(index instanceof Integer) {
			indexCollection = (Integer) index;
		} else {
			indexCollection = Integer.parseInt(String.valueOf(index));
		}

		if (collection != null && indexCollection < collection.size()) {
			context.put(toKey, collection.toArray()[indexCollection]);
		} else {
			context.remove(toKey);
		}

		return false;

	}

}
