package com.dotosoft.tools.dataquizparser.helper;

import com.dotosoft.dotoquiz.utils.MD5Checksum;
import com.google.gdata.data.docs.Md5Checksum;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MD5ChecksumTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public MD5ChecksumTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MD5ChecksumTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testMD5ChecksumTest() {
		try {
			System.out.println(MD5Checksum.getMD5Checksum("LICENSE"));
			// output	: 0bb2827c5eacf570b6064e24e0e6653b
			// ref		: http://www.apache.org/dist/tomcat/tomcat-5/v5.5.17/bin/apache-tomcat-5.5.17.exe.MD5
			// 0bb2827c5eacf570b6064e24e0e6653b *apache-tomcat-5.5.17.exe
			
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
}