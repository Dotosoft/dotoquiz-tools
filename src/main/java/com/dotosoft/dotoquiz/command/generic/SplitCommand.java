package com.dotosoft.dotoquiz.command.generic;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class SplitCommand implements Command {

	private String fromKey;
	private String separator;
	private String toKey;

	public void setFromKey(String fromKey) {
		this.fromKey = fromKey;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		String data = (String) BeanUtils.getProperty(context, fromKey);
		String[] results = data.split(separator);
		context.put(toKey, (List<String>) Arrays.asList(results));
		
		return false;
	}
}
