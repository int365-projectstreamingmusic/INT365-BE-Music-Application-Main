package com.application.exceptons;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class HttpResponseModel {
	private int responseModel;
	private HttpStatus httpStatus;
	private String details;
	private String message;
}
