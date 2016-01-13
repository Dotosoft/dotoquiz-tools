package com.dotosoft.dotoquiz.tools.quizparser.utils;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.dotosoft.dotoquiz.common.QuizConstant;
import com.dotosoft.dotoquiz.model.data.DataQuestions;
import com.dotosoft.dotoquiz.model.data.DataTopics;
import com.dotosoft.dotoquiz.model.data.DataTopicsQuestions;
import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;
import com.dotosoft.dotoquiz.tools.quizparser.config.Settings;
  
public class HibernateUtil {
  
    private static SessionFactory sessionFactory;
  
    public static SessionFactory buildSessionFactory(Settings setting) {
        try {
        	Properties prop= new Properties();
        	prop.setProperty("hibernate.connection.driver_class", setting.getConnection().getDriverClass());
        	prop.setProperty("hibernate.connection.url", setting.getConnection().getUrl());                                
        	prop.setProperty("hibernate.connection.username", setting.getConnection().getUser());     
        	prop.setProperty("hibernate.connection.password", setting.getConnection().getPassword());
        	prop.setProperty("hibernate.connection.pool_size", String.valueOf(setting.getConnection().getPoolSize()));
	        prop.setProperty("hibernate.dialect", setting.getDialect());
	        prop.setProperty("hibernate.hbm2ddl.auto", setting.getHbm2ddl());
	        prop.setProperty("hibernate.show_sql", setting.getShowSQL());
			
	        Configuration annotationConfig = new Configuration().addProperties(prop);
	        
	        for(String packageMap : setting.getMappingPackages()) {
	        	annotationConfig.addPackage( packageMap ); 
	        }
	        
	        for(String classMap : setting.getMappingClasses()) {
	        	annotationConfig.addAnnotatedClass( Class.forName(classMap) ); 
	        }
	        
	        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(annotationConfig.getProperties());
            sessionFactory = annotationConfig.buildSessionFactory(ssrb.build());
        	
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        
        return sessionFactory;
    }
  
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
  
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
    
    public static ParameterQuestionType getQuestionTypeByName(Session session, String name) {
    	Query q = session.createQuery("From ParameterQuestionType qt where qt.name = :questionTypeId");
    	q.setString("questionTypeId", name);
    	
    	ParameterQuestionType parameterQuestionType = (ParameterQuestionType) q.uniqueResult();
    	return parameterQuestionType;
    }
  
    public static void SaveOrUpdateTopicQuestionData(Session session, DataTopics topic, DataQuestions questionAnswer) {
    	Query q = session.createQuery("From DataTopicsQuestions tq where tq.datQuestions.id = :questionId and tq.datTopics.id = :topicId");
    	q.setString("questionId", questionAnswer.getId());
    	q.setString("topicId", topic.getId());
		List<DataTopicsQuestions> dataTopicQuestions = q.list();
		if(!dataTopicQuestions.isEmpty()) {
			for(DataTopicsQuestions topicQuestion : dataTopicQuestions) {
				topicQuestion.setDatQuestions(questionAnswer);
				topicQuestion.setDatTopics(topic);
				topicQuestion.setIsDelete(QuizConstant.NO);
				session.update(topicQuestion);
			}
		} else {
			DataTopicsQuestions topicQuestion = new DataTopicsQuestions();
			topicQuestion.setId(UUID.randomUUID().toString());
			topicQuestion.setDatQuestions(questionAnswer);
			topicQuestion.setDatTopics(topic);
			topicQuestion.setIsDelete(QuizConstant.NO);
			topicQuestion.setCreatedBy(QuizConstant.SYSTEM_USER);
			topicQuestion.setCreatedDt(new Date());
			session.save(topicQuestion);
		}
    }
}