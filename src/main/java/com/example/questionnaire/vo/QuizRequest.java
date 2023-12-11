package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;

public class QuizRequest extends QuizVo{
	
	private List<Question> deleteQuestionList;

	public QuizRequest() {
		super();
	}

	public QuizRequest(Questionnaire questionnaire, List<Question> questionList) {
		super(questionnaire, questionList);
	}

	public List<Question> getDeleteQuestionList() {
		return deleteQuestionList;
	}

	public void setDeleteQuestionList(List<Question> deleteQuestionList) {
		this.deleteQuestionList = deleteQuestionList;
	}
	
	

}
