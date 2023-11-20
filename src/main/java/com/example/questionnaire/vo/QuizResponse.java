package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;



public class QuizResponse {
	
	private List<QuizVo> quizVo;
	
	private RtnCode rtnCode;

	public QuizResponse() {
		super();
	}
	
	

	public QuizResponse(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}



	public QuizResponse(List<QuizVo> quizVo, RtnCode rtnCode) {
		super();
		this.quizVo = quizVo;
		this.rtnCode = rtnCode;
	}

	public List<QuizVo> getQuizVo() {
		return quizVo;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	
	
	
	

}
