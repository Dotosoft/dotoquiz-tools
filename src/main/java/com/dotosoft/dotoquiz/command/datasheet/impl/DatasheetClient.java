package com.dotosoft.dotoquiz.command.datasheet.impl;

import java.util.List;

public interface DatasheetClient {
	Object getWorksheet(int index) throws Exception;
	List getWorksheets() throws Exception;
	Object updateSheet(Object worksheetEntry) throws Exception;
	Object createNewSheet(String title, int col, int row) throws Exception;
	void deleteSheet(Object worksheetEntry) throws Exception;
	List getListRows(Object worksheet) throws Exception;
	void showColumnHeader(Object listEntry);
}
