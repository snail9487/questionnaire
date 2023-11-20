package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;

public class QuizRequest extends QuizVo{

	public QuizRequest() {
		super();
	}

	public QuizRequest(Questionnaire questionnaire, List<Question> questionList) {
		super(questionnaire, questionList);
	}
	
	
}
