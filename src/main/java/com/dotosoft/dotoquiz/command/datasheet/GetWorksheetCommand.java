package com.dotosoft.dotoquiz.command.datasheet;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import com.dotosoft.dotoquiz.command.datasheet.impl.DatasheetClient;
import com.dotosoft.dotoquiz.command.datasheet.impl.GooglesheetClient;
import com.dotosoft.dotoquiz.command.image.impl.ImageWebClient;
import com.dotosoft.dotoquiz.tools.util.BeanUtils;
import com.dotosoft.dotoquiz.tools.util.SingletonFactory;
import com.dotosoft.dotoquiz.utils.StringUtils;

public class GetWorksheetCommand implements Command {

	private String apiKey;
	private String indexKey;
	private String toKey;

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey;
	}

	public void setToKey(String toKey) {
		this.toKey = toKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {

		DatasheetClient webClient = (DatasheetClient) BeanUtils.getProperty(context, apiKey);
		if (webClient == null) {
			throw new Exception("Image API is not exist!");
		}

		Integer index = (Integer) BeanUtils.getProperty(context, indexKey);
		if (index != null) {
			context.put(toKey, webClient.getWorksheet(index));
		} else {
			context.put(toKey, webClient.getWorksheets());
		}

		return false;
	}

}
