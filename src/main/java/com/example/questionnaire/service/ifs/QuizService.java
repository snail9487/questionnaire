package com.example.questionnaire.service.ifs;

import java.time.LocalDate;
import java.util.List;

import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizRequest;
import com.example.questionnaire.vo.QuizResponse;

public interface QuizService {

	public QuizResponse create(QuizRequest req);
	
	public QuizResponse update(QuizRequest req);
	
	public QuizResponse deleteQuestionnaire(List<Integer> qnIdList);
	
	public QuizResponse deleteQuestion(int qnId, List<Integer> quIdList);
	
	public QuizResponse search(String title, LocalDate startDate, LocalDate endDate);
	
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate, boolean isAll);

	public QuestionRes searchQuestionList(int qnId);
}
