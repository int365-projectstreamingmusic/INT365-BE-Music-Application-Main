package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.entities.models.UserAccountModel;
import com.application.repositories.UserAccountModelRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class UserAccountController {

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;

	//OK!
	//Get user profile from Token
	public UserAccountModel getProfile(HttpServletRequest request) {
		UserAccountModel userProfile = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		return userProfile;
	}

}
