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
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizRequest;
import com.example.questionnaire.vo.QuizResponse;
import com.example.questionnaire.vo.QuizVo;

@SpringBootTest
class QuizServiceTest {

	@Autowired
	private QuizService service;
	
	@Autowired
	private QuestionnaireDao qnDao;
	
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
		Questionnaire questionnaire = new Questionnaire(10, "updatetest1", "test", false,
				LocalDate.of(2023,11, 17), LocalDate.of(2023, 11, 30));
		
		List<Question> questionList = new ArrayList<>();
		Question q1 = new Question(1, 10, "test_question_1", "single", false, "updateAAA;BBB;CCC");
		Question q2 = new Question(2, 10, "test_question_2", "multi", false, "update10;20;30;40");
		Question q3 = new Question(3, 10, "test_question_3", "test", false, "updateABC");
		questionList.addAll(Arrays.asList(q1, q2, q3));
		
		QuizRequest req = new QuizRequest(questionnaire, questionList);
		QuizResponse res = service.update(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"update error!");
	}
	
	@Test
	public void deleteQuestionnaireTest(){
		List<Integer> qnIdList = Arrays.asList(8, 9);
		QuizResponse res = service.deleteQuestionnaire(qnIdList);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"deleteQuestionnaire error!");
	}
		
	@Test
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
		for(QuizVo quiz : res.getQuizVoList()) {
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
	
	@Test
	public void sqlInsertTest() {
		int res = qnDao.insert("qa_01", "qa_01 test", false, LocalDate.of(2023, 11, 24), LocalDate.of(2024, 01, 02));
		System.out.println(res);
	}
	
	@Test
	public void sqlUpdateTest() {
		int res = qnDao.update(12, "qa_01_uupp", "qa_01_uuppp");
		System.out.println(res);
	}
	
	@Test
	public void updateDataTest() {
		int res = qnDao.updateData(11, "qaUptadeTest", "qaUptadeTest", LocalDate.of(2023, 12, 01));
		System.out.println(res);
	}

	@Test
	public void selectTest1() {
//		List<Questionnaire> res = qnDao.findByStartDate(LocalDate.of(2023, 11, 17));
//		List<Questionnaire> res = qnDao.findByStartDate1(LocalDate.of(2023, 11, 17));
//		List<Questionnaire> res = qnDao.findByStartDate2(LocalDate.of(2023, 11, 17));
//		List<Questionnaire> res = qnDao.findByStartDate3(LocalDate.of(2023, 11, 17), false);
//		List<Questionnaire> res = qnDao.findByStartDate4(LocalDate.of(2023, 11, 17), false);
		List<Questionnaire> res = qnDao.findByStartDate5(LocalDate.of(2023, 11, 17), false, 3);
		System.out.println(res.size());
	}
	
	@Test
	public void limitTest() {
		List<Questionnaire> res = qnDao.findWithLimitAndStartIndex(1,3);
		for(Questionnaire item : res) {
			System.out.println(item.getId());
		}
//		同上
//		res.forEach(item -> {
//			System.out.println(item.getId());
//		});
	}
	
	@Test
	public void likeTest() {
		List<Questionnaire> res = qnDao.searchTitleLike2("up");
		System.out.println(res.size());
		for(Questionnaire item : res) {
			System.out.println(item.getTitle());
		}
	}
	
	@Test
	public void regexpTest() {
		List<Questionnaire> res = qnDao.searchDescriptionContaining2("qa", "qn");
		System.out.println(res.size());
		for(Questionnaire item : res) {
			System.out.println(item.getDescription());
		}
	}
	
	@Test
	public void joinTest() {
		List<QnQuVo> res = qnDao.selectJoinQnQu();
		for(QnQuVo item : res) {
			System.out.printf("id: %d, title: %s, qu_id: %d", item.getId(), item.getqTitle(), item.getQuId());
			System.out.println();
		}
	}
	
	@Test
	public void selectFuzzyTest() {
		QuizResponse res = service.searchFuzzy("test", LocalDate.of(1971, 1, 1), LocalDate.of(2099, 1, 1));
		System.out.println(res.getQnQuVo().size());
	}
	
	@Test
	public void Test() {
		
	}
	
}
