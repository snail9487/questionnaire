package com.example.questionnaire.vo;

import java.util.List;

import com.example.questionnaire.constants.RtnCode;



public class QuizResponse {
	
	private List<QuizVo> quizVoList;
	
	private List<QnQuVo> qnQuVo;
	
	private RtnCode rtnCode;

	public QuizResponse() {
		super();
	}
	
	

	public QuizResponse(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	

	public QuizResponse(List<QuizVo> quizVoList, List<QnQuVo> qnQuVo, RtnCode rtnCode) {
		super();
		this.quizVoList = quizVoList;
		this.qnQuVo = qnQuVo;
		this.rtnCode = rtnCode;
	}



	public QuizResponse(List<QuizVo> quizVoList, RtnCode rtnCode) {
		super();
		this.quizVoList = quizVoList;
		this.rtnCode = rtnCode;
	}

	public List<QuizVo> getQuizVoList() {
		return quizVoList;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}



	public List<QnQuVo> getQnQuVo() {
		return qnQuVo;
	}



	public void setQnQuVo(List<QnQuVo> qnQuVo) {
		this.qnQuVo = qnQuVo;
	}



	public void setQuizVo(List<QuizVo> quizVoList) {
		this.quizVoList = quizVoList;
	}



	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

	
	
	
	

}
