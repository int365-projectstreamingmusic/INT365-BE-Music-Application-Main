package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.UserAccountModel;
import com.application.repositories.UserAccountRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class UserProfileController {

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private FileLinkRelController fileLinkRefController;

	// OK!
	// setNewBio
	public String setNewBio(String newBio, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		userAccountRepository.updateUserBio(newBio, targetUser.getAccountId());
		return newBio;
	}

	// OK!
	// setNewFirstName
	public String setNewFirstName(String newFirstName, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		userAccountRepository.updateUserFirstName(newFirstName, targetUser.getAccountId());
		return newFirstName;
	}

	// OK!
	// setNewLastName
	public String setNewLastName(String newLastName, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		userAccountRepository.updateUserLastName(newLastName, targetUser.getAccountId());
		return newLastName;
	}

	// OK!
	// setNewProfileName
	public String setNewProfileName(String newProfileName, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		userAccountRepository.updateUserProfileName(newProfileName, targetUser.getAccountId());
		return newProfileName;
	}

	// setNewUserProfileImage
	public String setNewUserProfileImage(MultipartFile prodileImageFile, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (targetUser.getProfileIamge() != null) {
			try {
				fileLinkRefController.deleteTargetFileByTypeIdAndLinkRef(101, targetUser.getAccountId());
			} catch (Exception exc) {
				System.out.println("[ UserProfileController ] Excaption is here but everything is fine. :D");
			}
		}
		String profileImageFileName = fileLinkRefController.insertNewTrackObjectLinkRel(prodileImageFile, 101,
				targetUser.getAccountId());
		userAccountRepository.updateUserProfileImage(profileImageFileName, targetUser.getAccountId());
		return "Updated!";

	}

	// ---- WIP ----

	// requestAccountDeletion
	public void requestAccountDeletion(String password, HttpServletRequest request) {

	}

}
