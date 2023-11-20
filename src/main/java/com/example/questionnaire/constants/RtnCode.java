package com.example.questionnaire.constants;


public enum RtnCode {

	SUCCESSFUL(200, "OK!"), //
	PARAM_ERROR(400, "Param error!"), //
	QUESTIONNAIRE_PARAM_ERROR(400,"Questionnaire param error!"), //
	QUESTION_PARAM_ERROR(400,"Question param error!"), //
	QUESTIONNAIRE_ID_PARAM_ERROR(400,"Questionnaire id param error!"), //
	QUESTIONNAIRE_ID_NOT_FOUND(404,"Questionnaire id not found!"), //
	UPDARE_ERROR(400,"Update error!"), //
	
	;
	
	private int code;
	
	private String message;

	
	
	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	
	
}
