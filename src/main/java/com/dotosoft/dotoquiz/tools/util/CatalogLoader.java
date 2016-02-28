/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

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
