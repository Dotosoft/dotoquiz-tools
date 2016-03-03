package com.dotosoft.dotoquiz.tools;

import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.chain.Command;

import com.dotosoft.dotoquiz.tools.config.DotoQuizContext;
import com.dotosoft.dotoquiz.tools.util.CatalogLoader;
import com.google.gdata.util.ServiceException;

/**
 * Unit test for simple App.
 */
public class AppTest  extends TestCase
{	
	private String[] args = new String[]{"TEST", "settings.yaml"};
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws ServiceException 
     * @throws IOException 
     */
    public void testOldApp() throws IOException, ServiceException
    {
    	try {
			DotoQuizContext ctx = new DotoQuizContext();
			if( ctx.getSettings().loadSettings(args) ) {
				Command command = CatalogLoader.getInstance().getCatalog().getCommand(ctx.getSettings().getApplicationType());
				if(command != null) {
					command.execute(ctx);
					assertTrue( true );
					return;
				}
			} 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        assertTrue( false );
    }
}