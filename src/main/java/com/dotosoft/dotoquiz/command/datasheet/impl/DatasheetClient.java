package com.dotosoft.dotoquiz.command.datasheet.impl;

import java.util.List;

public interface DatasheetClient {
	// Get Worksheet
	Object getWorksheet(int index) throws Exception;
	List getWorksheets() throws Exception;
	// Update/Delete Worksheet
	Object updateSheet(Object worksheetEntry) throws Exception;
	void deleteSheet(Object worksheet) throws Exception;
	// Create Worksheet
	Object createNewSheet(String title, int col, int row) throws Exception;
	// Get List Row
	List getListRows(Object worksheet) throws Exception;
	// Show Column Header
	void showColumnHeader(Object listEntry);
}
