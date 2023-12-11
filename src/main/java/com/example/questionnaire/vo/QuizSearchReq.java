package com.example.questionnaire.vo;

import java.time.LocalDate;

//import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizSearchReq {

	private String title;

//	@JsonProperty("start_date")
	private LocalDate startDate;
	
//	@JsonProperty("end_date")
	private LocalDate endDate;
	
	private boolean published;

	public QuizSearchReq() {
		super();
	}

	
	
	public QuizSearchReq(String title, LocalDate startDate, LocalDate endDate, boolean published) {
		super();
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.published = published;
	}



	public QuizSearchReq(String title, LocalDate startDate, LocalDate endDate) {
		super();
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}
	
	
	
	
}
