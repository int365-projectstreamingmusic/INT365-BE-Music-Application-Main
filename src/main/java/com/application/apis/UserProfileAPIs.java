package com.application.apis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.UserAccountController;
import com.application.entities.models.UserAccountModel;

@RestController
@RequestMapping("api/profile/")
public class UserProfileAPIs {

	@Autowired
	private UserAccountController userAccountController;

	@GetMapping("myprofile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest servletRequest) {
		return ResponseEntity.ok().body(userAccountController.getProfile(servletRequest));
	}

}
