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

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.yaml.snakeyaml.Yaml;

import com.dotosoft.dotoquiz.config.Configuration;

public class ConfigYamlTest extends TestCase {
	
	String args[] = new String[]{"settings.yaml"};
	
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ConfigYamlTest.class);
	}
	
	// mvn compile exec:java -Dexec.mainClass=com.dotosoft.hibernate.HibernateTest
	public void testYamlConfigRunner() throws IOException {
		if( args.length != 1 ) {
            System.out.println( "Usage: <file.yml>" );
            return;
        }
  
        Yaml yaml = new Yaml();  
        try( InputStream in = Files.newInputStream( Paths.get( args[ 0 ] ) ) ) {
            Configuration config = yaml.loadAs( in, Configuration.class );
            System.out.println( config.toString() );
            
            // config.setVersion("99");
            
            Writer writer = new FileWriter( "output-" + args[ 0 ] );
            yaml.dump(config, writer);
            writer.close();
        }
        
	}
}
