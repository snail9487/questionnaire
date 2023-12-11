package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QnQuVo;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizRequest;
import com.example.questionnaire.vo.QuizResponse;
import com.example.questionnaire.vo.QuizVo;

@EnableScheduling
@Service
public class QuizServiceImpl implements QuizService{
	
	@Autowired
	private QuestionnaireDao qnDao;
	
	@Autowired
	private QuestionDao quDao;

	@Transactional
	@Override
	public QuizResponse create(QuizRequest req) {
		QuizResponse checkResult = checkParam(req);
		if(checkResult != null) {
			return checkResult;
		}
		int qnId = qnDao.save(req.getQuestionnaire()).getId();
		List<Question> quList = req.getQuestionList();
		if(quList.isEmpty()) {
			return new QuizResponse(RtnCode.SUCCESSFUL);
		}
		for(Question qu : quList) {
			qu.setQnId(qnId);
		}
		quDao.saveAll(quList);
		return new QuizResponse(RtnCode.SUCCESSFUL);
	}
	
	
	private QuizResponse checkParam(QuizRequest req) {
		Questionnaire qn = req.getQuestionnaire();
		if(!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null
				|| qn.getStartDate().isAfter(qn.getEndDate())) {
			return new QuizResponse(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for(Question qu : quList) {
			if(qu.getQuId() <= 0 || !StringUtils.hasText(qu.getqTitle()) ||
					!StringUtils.hasText(qu.getOption()) || !StringUtils.hasText(qu.getOptionType())) {
				return new QuizResponse(RtnCode.QUESTION_PARAM_ERROR);
			}
		}
		
		return null;
	}


	@Transactional
	@Override
	public QuizResponse update(QuizRequest req) {
		QuizResponse checkResult = checkParam(req);
		if(checkResult != null) {
			return checkResult;
		}
		checkResult = checkQuestionnaireId(req);
		if(checkResult !=null) {
			return checkResult;
		}
//		if (!qnDao.existsById(req.getQuestionnaire().getId())) {
//			return new QuizResponse(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
//		}
		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if(qnOp.isEmpty()) {
			return new QuizResponse(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}
		Questionnaire qn = qnOp.get();
		//可以更改的問卷:
		//1.未發布 
		//2.已發布且未開始
//		if(!req.getQuestionnaire().isPublished() )
		if(!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizResponse(RtnCode.SUCCESSFUL);
		}
		return new QuizResponse(RtnCode.UPDARE_ERROR);
	}
	
	private QuizResponse checkQuestionnaireId(QuizRequest req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizResponse(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizResponse(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		return null;
	}


	@Transactional
	@Override
	public QuizResponse deleteQuestionnaire(List<Integer> qnIdList) {
		List<Questionnaire> qnList = qnDao.findByIdIn(qnIdList);
		List<Integer> idList = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
//				qnDao.deleteById(qn.getId());
				idList.add(qn.getId());
			}
		}
		if(!idList.isEmpty()) {
			qnDao.deleteAllById(idList);
			quDao.deleteAllByQnIdIn(idList);
		}
		return new QuizResponse(RtnCode.SUCCESSFUL);
	}


	@Transactional
	@Override
	public QuizResponse deleteQuestion(int qnId, List<Integer> quIdList) {
		Optional<Questionnaire> qnOp = qnDao.findById(qnId);
		if(qnOp.isEmpty()) {
			return new QuizResponse(RtnCode.SUCCESSFUL);
		}
		Questionnaire qn = qnOp.get();
		if(!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
			//quDao.deleteAllByQuIdIn(quIdList);
			quDao.deleteAllByQnIdAndQuIdIn(qnId , quIdList);
		}
		return new QuizResponse(RtnCode.SUCCESSFUL);
	}


//	@Cacheable(cacheNames = "search",
//			key = "#title.concat('-')",
//			unless = "")
	@Override
	public QuizResponse search(String title, LocalDate startDate, LocalDate endDate) {
//		if(startDate == null) {
//			startDate = LocalDate.of(1971, 1, 1);
//		}
//		if(endDate == null) {
//			endDate = LocalDate.of(2099, 12, 31);
//		}
//		if(!StringUtils.hasText(title)) {
//			title = "";
//		}
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		
		List<Questionnaire> qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		if(qnList.isEmpty()) {
			return new QuizResponse(null, RtnCode.SUCCESSFUL);
		}
		List<Integer> qnIds = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			qnIds.add(qn.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds);
		List<QuizVo> quizVoList = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();
			vo.setQuestionnaire(qn);
			List<Question> questionList = new ArrayList<>();
			for(Question qu : quList) {
				if (qu.getQnId() == qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}
		return new QuizResponse(quizVoList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizResponse search1(String title, LocalDate startDate, LocalDate endDate, boolean isAll) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		
		List<Questionnaire> qnList = new ArrayList<>();
		if(!isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(title, startDate, endDate);
		}
		else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		}
		if(qnList.isEmpty()) {
			return new QuizResponse(null, RtnCode.SUCCESSFUL);
		}
		List<Integer> qnIds = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			qnIds.add(qn.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds);
		List<QuizVo> quizVoList = new ArrayList<>();
		for(Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();
			vo.setQuestionnaire(qn);
			List<Question> questionList = new ArrayList<>();
			for(Question qu : quList) {
				if (qu.getQnId() == qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}
		return new QuizResponse(quizVoList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,  boolean isAll) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate != null ? startDate : LocalDate.of(1971, 1, 1);
		endDate = endDate != null ? endDate : LocalDate.of(2099, 12, 31);
		List<Questionnaire> qnList = new ArrayList<>();
		if(!isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(title, startDate, endDate);
		}
		else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		}
		
		return new QuestionnaireRes(qnList, RtnCode.SUCCESSFUL);
	}


	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if(qnId <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		return new QuestionRes(quList, RtnCode.SUCCESSFUL);
	}


	@Override
	public QuizResponse searchFuzzy(String title, LocalDate startDate, LocalDate endDate) {
		 
		List<QnQuVo> res = qnDao.selectFuzzy(title, startDate, endDate);
		return new QuizResponse(null, res, RtnCode.SUCCESSFUL);
	}

	
	//排程
	//				   秒分時日月周 (周1-7 = Sun-Sat)
	@Scheduled(cron = "0/5 * 14 * * * ")
	public void schedule() {
		System.out.println(LocalDateTime.now());
	}

	
	
	
}
