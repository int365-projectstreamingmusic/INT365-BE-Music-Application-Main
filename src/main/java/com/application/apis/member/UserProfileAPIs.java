package com.application.apis.member;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.UserAccountManagerController;
import com.application.entities.models.UserAccountModel;

@RestController
@RequestMapping("api/profile/")
public class UserProfileAPIs {

	@Autowired
	private UserAccountManagerController userAccountController;

	@GetMapping("myprofile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest servletRequest) {
		return ResponseEntity.ok().body(userAccountController.getProfile(servletRequest));
	}

}
