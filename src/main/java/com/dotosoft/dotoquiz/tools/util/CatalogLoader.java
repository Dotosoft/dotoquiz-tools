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

import com.dotosoft.dot4command.chain.Catalog;
import com.dotosoft.dot4command.chain.Context;
import com.dotosoft.dot4command.config.ConfigParser;
import com.dotosoft.dot4command.config.xml.XmlConfigParser;
import com.dotosoft.dot4command.impl.CatalogBase;
import com.dotosoft.dot4command.impl.CatalogFactoryBase;
import com.dotosoft.dot4command.impl.ContextBase;

public class CatalogLoader {
	private static final String CONFIG_FILE = "/chain-config.xml";

	private ConfigParser parser;
	private static CatalogLoader catalogLoader;

	private CatalogLoader() {
		CatalogFactoryBase.clear();
	    Catalog<String, Object, Context<String, Object>> catalog = new CatalogBase<String, Object, Context<String, Object>>();
	    Context<String, Object> context = new ContextBase();
		parser = new XmlConfigParser();
	}
	
	public static CatalogLoader getInstance() {
		if(catalogLoader == null) {
			catalogLoader = new CatalogLoader();
			catalogLoader.parseCatalog();
		}
		return catalogLoader;
	}
	
	private void parseCatalog() {
		try {
			parser.parse(this.getClass().getResource(CONFIG_FILE));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Catalog getCatalog() throws Exception {
		return getCatalog(EMPTY_STRING);
	}

	public Catalog getCatalog(String name) throws Exception {
		if (name.equals("")) {
			return CatalogFactoryBase.getInstance().getCatalog();
		} else {
			return CatalogFactoryBase.getInstance().getCatalog(name);
		}
	}
}
