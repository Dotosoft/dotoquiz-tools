package com.dotosoft.dotoquiz.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;

import com.dotosoft.dotoquiz.tools.config.DotoQuizContext;
import com.dotosoft.dotoquiz.tools.util.CatalogLoader;

public class App {
	
	Catalog quizParserCatalog;
	
	public App(String args[]) {
		try {
			CatalogLoader loader = new CatalogLoader();
			quizParserCatalog = loader.getCatalog();
			
			DotoQuizContext ctx = new DotoQuizContext();
			if( ctx.getSettings().loadSettings(args) ) {
				Command command = quizParserCatalog.getCommand(ctx.getSettings().getApplicationType());
				command.execute(ctx);
			} else {
				showError();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			showError();
		}
	}
	
	public void showError() {
		System.err.println("Error: Could not run SyncQuizParser.");
		System.err.println("Run: java -jar SyncQuizParser.jar "+ Arrays.toString(listCommands()) + " [file config]");
	}
	
	public Object[] listCommands() {
		Iterator<String> keyCommand = quizParserCatalog.getNames();
		List<String> commands = new ArrayList<String>();
		while(keyCommand.hasNext()) {
			commands.add(keyCommand.next());
		}
		return commands.toArray();
	}
	
	public static void main(String args[]) {
		new App(args);
	}
}
