package com.application.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("test/")
public class TestingApis {
	
	@Autowired
	private ValidatorServices validatorServices;

	@GetMapping("1")
	public boolean testValidator(@RequestBody String testText) {
		return validatorServices.validatePassword(testText);
	}
}
