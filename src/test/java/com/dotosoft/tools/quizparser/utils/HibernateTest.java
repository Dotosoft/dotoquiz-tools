package com.dotosoft.tools.quizparser.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Query;
import org.hibernate.Session;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;
import com.dotosoft.dotoquiz.tools.quizparser.helper.MD5Checksum;
import com.dotosoft.dotoquiz.tools.quizparser.utils.HibernateUtil;
import com.dotosoft.tools.dataquizparser.helper.MD5ChecksumTest;

public class HibernateTest extends TestCase {

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(HibernateTest.class);
	}
	
	// mvn compile exec:java -Dexec.mainClass=com.dotosoft.hibernate.HibernateTest
	public void testHibernate() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		
		Query q = session.createQuery("From ParameterQuestionType");
		List<ParameterQuestionType> test = q.list();
		
		for(ParameterQuestionType parameter : test) {
			System.out.println(parameter);
		}
	}
}
