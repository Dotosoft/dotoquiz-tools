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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.hibernate.Query;
import org.hibernate.Session;

import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;

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
