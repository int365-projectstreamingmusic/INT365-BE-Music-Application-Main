package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.FileLinkRefModel;
import com.application.entities.models.FileTypeModel;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.FileLinkRefRepository;
import com.application.repositories.FileTypeRepository;
import com.application.repositories.UserAccountRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class UserProfileController {

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private FileTypeRepository fileTypeRepository;
	@Autowired
	private FileLinkRelController fileLinkRelController;
	@Autowired
	private FileLinkRefRepository fileLinkRefRepository;

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
		userAccountRepository.updateUserLastName(newProfileName, targetUser.getAccountId());
		return newProfileName;
	}

	// setNewUserProfileImage
	public String setNewUserProfileImage(MultipartFile prodileImageFile, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (targetUser.getProfileIamge() != null) {
			FileLinkRefModel targetFile = fileLinkRefRepository.findByTargetRefAndTypeId(101,
					targetUser.getAccountId());
			if (targetFile != null) {
				fileLinkRefRepository.deleteByTargetRefAndTypeId(101, targetUser.getAccountId());
			}
		}
		String profileImageFileName = fileLinkRelController.insertNewTrackObjectLinkRel(prodileImageFile, 101,
				targetUser.getAccountId());
		userAccountRepository.updateUserProfileName(profileImageFileName, targetUser.getAccountId());
		return "Updated!";

	}

	// ---- WIP ----

	// requestAccountDeletion
	public void requestAccountDeletion(String password, HttpServletRequest request) {

	}

}
