package com.dotosoft.dotoquiz.command.generic;

import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

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
		if(context.containsKey(collectionKey)) {
			Integer index = (Integer) context.get(incrementKey);
			if(index == null) {
				index = 0;
			}
			
			List coll = (List) context.get(collectionKey);
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
