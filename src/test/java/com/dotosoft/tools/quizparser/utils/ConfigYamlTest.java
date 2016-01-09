package com.dotosoft.tools.quizparser.utils;

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
