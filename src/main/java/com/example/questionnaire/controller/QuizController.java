package com.example.questionnaire.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuizRequest;
import com.example.questionnaire.vo.QuizResponse;
import com.example.questionnaire.vo.QuizSearchReq;

@RestController
public class QuizController {
	
	@Autowired
	private QuizService service;
	
	@PostMapping(value = "api/quiz/create")
	public QuizResponse create(@RequestBody QuizRequest req) {
		return service.create(req);
	}
	
	@PostMapping(value = "api/quiz/search")
	public QuizResponse search(@RequestBody QuizSearchReq req) {
		String title = StringUtils.hasText(req.getTitle()) ? req.getTitle() : "";
		LocalDate startDate = req.getStartDate() != null ? req.getStartDate() : LocalDate.of(1971, 1, 1);
		LocalDate endDate = req.getEndDate() != null ? req.getEndDate() : LocalDate.of(2099, 12, 31);
		return service.search(title, startDate, endDate);
	}
	
	@PostMapping(value = "api/quiz/search1")
	public QuizResponse search1(@RequestBody QuizSearchReq req) {
		String title = StringUtils.hasText(req.getTitle()) ? req.getTitle() : "";
		LocalDate startDate = req.getStartDate() != null ? req.getStartDate() : LocalDate.of(1971, 1, 1);
		LocalDate endDate = req.getEndDate() != null ? req.getEndDate() : LocalDate.of(2099, 12, 31);
		boolean published = req.isPublished();
		return service.search1(title, startDate, endDate, published);
	}
	
	@PostMapping(value = "api/quiz/deleteQuestionnaire")
	public QuizResponse deleteQuestionnaire(@RequestBody List<Integer> qnIdList) {
		return service.deleteQuestionnaire(qnIdList);
	}
	
	
}
	