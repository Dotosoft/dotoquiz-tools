package com.dotosoft.dotoquiz.command.datasheet;

import java.util.List;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.command.datasheet.impl.DatasheetClient;
import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.BeanUtils;

public class ListEntrysheetCommand implements Command {

	private String apiKey;
	private String dataKey;
	private String toKey;

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {
		DatasheetClient webClient = (DatasheetClient) BeanUtils.getProperty(context, apiKey);
		if (webClient == null) {
			throw new Exception("API is not exist!");
		}

		Object value = BeanUtils.getProperty(context, dataKey);
		if(value != null) {
			List listRow = webClient.getListRows(value);
			context.put(toKey, listRow);
		} else {
			context.remove(toKey);
		}

		return false;
	}

}
