package com.application.apis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.TrackManagerController;
import com.application.controllers.TrackMarkingController;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("test/")
public class TestingApis {
	
	@Autowired
	private ValidatorServices validatorServices;
	@Autowired
	private TrackMarkingController trackMarkingController;

	@GetMapping("1")
	public boolean testValidator(@RequestBody String testText) {
		return validatorServices.validatePassword(testText);
	}
	
	@GetMapping("2")
	public String do2(@RequestParam int trackId, HttpServletRequest request) {
		trackMarkingController.removeFavoriteTrack(trackId, request);
		return "OK";
	}
}
