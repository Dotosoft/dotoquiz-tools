package com.dotosoft.tools.quizparser.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Query;
import org.hibernate.Session;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;
import com.dotosoft.tools.dataquizparser.helper.MD5ChecksumTest;
import com.dotosoft.tools.quizparser.helper.MD5Checksum;
import com.dotosoft.tools.quizparser.utils.HibernateUtil;

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

//		Department department = new Department("java");
//		session.save(department);
//
//		session.save(new Employee("Jakab Gipsz", department));
//		session.save(new Employee("Captain Nemo", department));
//
//		session.getTransaction().commit();
//
//		Query q = session.createQuery("From Employee ");
//
//		List<Employee> resultList = q.list();
//		System.out.println("num of employess:" + resultList.size());
//		for (Employee next : resultList) {
//			System.out.println("next employee: " + next);
//		}
	}
}
