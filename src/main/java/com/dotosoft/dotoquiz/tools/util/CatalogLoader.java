package com.dotosoft.dotoquiz.tools.util;

import static com.dotosoft.dotoquiz.common.QuizConstant.EMPTY_STRING;

import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogFactoryBase;

public class CatalogLoader {
	private static final String CONFIG_FILE = "/chain-config.xml";

	private ConfigParser parser;
	private Catalog catalog;

	public CatalogLoader() {
		parser = new ConfigParser();
	}

	public Catalog getCatalog() throws Exception {
		return getCatalog(EMPTY_STRING);
	}

	public Catalog getCatalog(String name) throws Exception {
		if (catalog == null) {
			parser.parse(this.getClass().getResource(CONFIG_FILE));
		}

		if (name.equals("")) {
			catalog = CatalogFactoryBase.getInstance().getCatalog();
		} else {
			catalog = CatalogFactoryBase.getInstance().getCatalog(name);
		}

		return catalog;
	}
}
