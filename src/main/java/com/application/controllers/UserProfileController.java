package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.utilities.JwtTokenUtills;

@Service
public class UserProfileController {

	public void setNewBio(String newBio, HttpServletRequest request) {
		String username = JwtTokenUtills.getUserNameFromToken(request);
	}

	public void setNewFirstName(String newFirstName, HttpServletRequest request) {

	}

	public void setNewLastName(String newLastName, HttpServletRequest request) {

	}

	public void setNewPassword(String oldpassword) {

	}

	public void newUserProfile(MultipartFile prodileImageFile, HttpServletRequest request) {

	}
	
	

	// DELETE ACCOUNT
	public void requestAccountDeletion(String password, HttpServletRequest request) {

	}

}
