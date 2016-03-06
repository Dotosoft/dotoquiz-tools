package com.dotosoft.dotoquiz.command.datasheet;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public class SubmitSheetCommand implements Command {

	public enum COMMAND_SHEET {
		NONE, DELETE, UPDATE
	}

	private String apiKey;
	private String dataKey;
	private COMMAND_SHEET commandKey = COMMAND_SHEET.NONE;

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setCommandKey(COMMAND_SHEET commandKey) {
		this.commandKey = commandKey;
	}

	@Override
	public boolean execute(Context context) throws Exception {

//		DatasheetClient webClient = (DatasheetClient) BeanUtils.getProperty(
//				context, apiKey);
//		if (webClient == null) {
//			throw new Exception("Image API is not exist!");
//		}
//
//		if (commandKey != COMMAND_SHEET.NONE) {
//			Object worksheet = BeanUtils.getProperty(context, dataKey);
//			if (commandKey != COMMAND_SHEET.UPDATE) {
//				context.put(dataKey, webClient.updateSheet(worksheet));
//			} else {
//				webClient.deleteSheet(worksheet);
//				context.remove(dataKey);
//			}
//		}

		return false;
	}

}
