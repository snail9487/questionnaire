package com.example.questionnaire;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizRequest;
import com.example.questionnaire.vo.QuizResponse;
import com.example.questionnaire.vo.QuizVo;

@SpringBootTest
class QuizServiceTest {

	@Autowired
	private QuizService service;
	
	@Test
	public void createTest() {
		Questionnaire questionnaire = new Questionnaire("test1", "test", false,
				LocalDate.of(2023,11, 27), LocalDate.of(2023, 11, 30));
		
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1, "test_question_1", "single", false, "AAA;BBB;CCC");
		Question q2 = new Question(2, "test_question_2", "multi", false, "10;20;30;40");
		Question q3 = new Question(3, "test_question_3", "test", false, "ABC");
		questionList.addAll(Arrays.asList(q1, q2, q3));
		
		QuizRequest req = new QuizRequest(questionnaire, questionList);
		QuizResponse res = service.create(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"create error!"); //條件為true 繼續執行,否則拋出右邊訊息
		
	}
	
	@Test
	public void updateTest() {
		Questionnaire questionnaire = new Questionnaire(3, "updatetest1", "test", false,
				LocalDate.of(2023,11, 17), LocalDate.of(2023, 11, 30));
		
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1, 3, "test_question_1", "single", false, "updateAAA;BBB;CCC");
		Question q2 = new Question(2, 3, "test_question_2", "multi", false, "update10;20;30;40");
		Question q3 = new Question(3, 3, "test_question_3", "test", false, "updateABC");
		questionList.addAll(Arrays.asList(q1, q2, q3));
		
		QuizRequest req = new QuizRequest(questionnaire, questionList);
		QuizResponse res = service.update(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"update error!");
	}
	
	@Test //error
	public void deleteQuestionnaireTest(){
		List<Integer> qnIdList = Arrays.asList(5, 6);
		QuizResponse res = service.deleteQuestionnaire(qnIdList);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"deleteQuestionnaire error!");
	}
		
	@Test //error
	public void deleteQuestionTest() {	
		int qnId = 3;
		List<Integer> quIdList = Arrays.asList(1, 2);
		QuizResponse res = service.deleteQuestion(qnId, quIdList);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"deleteQuestion error!");
		
	}
	
	@Test
	public void searchTest() {
		String title = "";
		LocalDate startDate = LocalDate.of(2023,11, 27);
		LocalDate endDate = null;
		QuizResponse res = service.search(title, startDate, endDate);
		for(QuizVo quiz : res.getQuizVo()) {
			System.out.println(quiz.getQuestionnaire().getId());
			for(Question qu : quiz.getQuestionList()) {
				System.out.println(qu.getQuId());
			}
		}
	}
	
	@Test
	public void searchQuestionnaireListTest() {
		String title = "";
		LocalDate startDate = LocalDate.of(2023,11, 27);
		LocalDate endDate = null;
		boolean isAll = true;
		QuestionnaireRes res = service.searchQuestionnaireList(title, startDate, endDate, isAll);
		for(Questionnaire qn : res.getQuestionnaireList()) {
			System.out.println(qn.getId());
		}
	}
	
	@Test
	public void searchQuestionListTest() {
		int qnId = 8;
		QuestionRes res = service.searchQuestionList(qnId);
		for(Question qu : res.getQuestionList()) {
			System.out.println(qu.getQuId() + " " + qu.getqTitle());
		}
	}

}
