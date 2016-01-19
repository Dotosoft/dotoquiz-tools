package com.dotosoft.dotoquiz.tools.quizparser.config;

import static java.lang.String.format;

import com.dotosoft.dotoquiz.tools.quizparser.config.model.AchievementStructure;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.AnswerQuestionStructure;
import com.dotosoft.dotoquiz.tools.quizparser.config.model.TopicStructure;

public class StructureConfig {
	private TopicStructure topicStructure;
	private AnswerQuestionStructure answerQuestionStructure;
	private AchievementStructure achievementStructure;

	private String tabAchievements;
	private String tabQuestions;
	private String tabTopics;

	public TopicStructure getTopicStructure() {
		return topicStructure;
	}

	public void setTopicStructure(TopicStructure topicStructure) {
		this.topicStructure = topicStructure;
	}

	public AnswerQuestionStructure getAnswerQuestionStructure() {
		return answerQuestionStructure;
	}

	public void setAnswerQuestionStructure(
			AnswerQuestionStructure answerQuestionStructure) {
		this.answerQuestionStructure = answerQuestionStructure;
	}

	public AchievementStructure getAchievementStructure() {
		return achievementStructure;
	}

	public void setAchievementStructure(
			AchievementStructure achievementStructure) {
		this.achievementStructure = achievementStructure;
	}

	public String getTabAchievements() {
		return tabAchievements;
	}

	public void setTabAchievements(String tabAchievements) {
		this.tabAchievements = tabAchievements;
	}

	public String getTabQuestions() {
		return tabQuestions;
	}

	public void setTabQuestions(String tabQuestions) {
		this.tabQuestions = tabQuestions;
	}

	public String getTabTopics() {
		return tabTopics;
	}

	public void setTabTopics(String tabTopics) {
		this.tabTopics = tabTopics;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("Structure Configurations:\n")
			.append(format("topicStructure: %s\n", topicStructure))
			.append(format("answerQuestionStructure: %s\n", answerQuestionStructure))
			.append(format("achievementStructure: %s\n", achievementStructure))
			.append(format("tabAchievements: %s\n", tabAchievements))
			.append(format("tabQuestions: %s\n", tabQuestions))
			.append(format("tabTopics: %s\n", tabTopics))
			.toString();
	}

}
