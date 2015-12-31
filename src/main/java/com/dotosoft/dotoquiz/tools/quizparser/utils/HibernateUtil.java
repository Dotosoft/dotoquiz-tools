package com.dotosoft.dotoquiz.tools.quizparser.utils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.id.UUIDGenerator;

import com.dotosoft.dotoquiz.model.data.DataQuestions;
import com.dotosoft.dotoquiz.model.data.DataTopics;
import com.dotosoft.dotoquiz.model.data.DataTopicsQuestions;
import com.dotosoft.dotoquiz.model.parameter.ParameterQuestionType;
import com.dotosoft.dotoquiz.tools.quizparser.config.QuizParserConstant;
  
public class HibernateUtil {
  
    private static final SessionFactory sessionFactory = buildSessionFactory();
  
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
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
				topicQuestion.setIsDelete(QuizParserConstant.NO);
				session.update(topicQuestion);
			}
		} else {
			DataTopicsQuestions topicQuestion = new DataTopicsQuestions();
			topicQuestion.setId(UUID.randomUUID().toString());
			topicQuestion.setDatQuestions(questionAnswer);
			topicQuestion.setDatTopics(topic);
			topicQuestion.setIsDelete(QuizParserConstant.NO);
			topicQuestion.setCreatedBy(QuizParserConstant.SYSTEM_USER);
			topicQuestion.setCreatedDt(new Date());
			session.save(topicQuestion);
		}
    }
}