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

package com.dotosoft.dotoquiz.tools.config;

import static java.lang.String.format;

import com.dotosoft.dotoquiz.tools.config.metadata.AchievementStructure;
import com.dotosoft.dotoquiz.tools.config.metadata.AnswerQuestionStructure;
import com.dotosoft.dotoquiz.tools.config.metadata.TopicStructure;

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
