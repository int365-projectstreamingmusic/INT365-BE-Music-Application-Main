package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.UserProfileForm;
import com.application.repositories.UserAccountRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.JwtTokenUtills;

@Service
public class UserProfileController {

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private FileLinkRelController fileLinkRefController;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// getMyProfile
	public UserAccountModel getMyProfile(HttpServletRequest request) {
		return generalFunctionController.getUserAccount(request);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// editBasicProfileInfo
	public UserAccountModel editBasicProfileInfo(UserProfileForm form, MultipartFile image,
			HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		if (form.getFirstName() != null && form.getFirstName() != "") {
			// userAccountRepository.updateUserFirstName(form.getFirstName(),
			// targetUser.getAccountId());
			targetUser.setFirstName(form.getFirstName());
		}
		if (form.getLastName() != null && form.getLastName() != "") {
			// userAccountRepository.updateUserLastName(form.getLastName(),
			// targetUser.getAccountId());
			targetUser.setLastName(form.getLastName());
		}
		if (form.getProfileName() != null && form.getProfileName() != "") {
			// userAccountRepository.updateUserProfileName(form.getProfileName(),
			// targetUser.getAccountId());
			targetUser.setProfileName(form.getProfileName());
		}
		if (form.getUserBios() != null && form.getUserBios() != "") {
			// userAccountRepository.updateUserBio(form.getUserBios(),
			// targetUser.getAccountId());
			targetUser.setUserBios(form.getUserBios());
		}
		targetUser = userAccountRepository.save(targetUser);
		if (image != null) {
			if (targetUser.getProfileIamge() != null) {
				try {
					fileLinkRefController.deleteTargetFileByTypeIdAndLinkRef(101, targetUser.getAccountId());
				} catch (Exception exc) {
					System.out.println("[ UserProfileController ] Excaption is here but everything is fine. :D");
				}
			}
			String profileImageFileName = fileLinkRefController.insertNewTrackObjectLinkRel(image, 101,
					targetUser.getAccountId());
			userAccountRepository.updateUserProfileImage(profileImageFileName, targetUser.getAccountId());
		}

		return targetUser;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// setNewUserProfileImage
	/*public String setNewUserProfileImage(MultipartFile prodileImageFile, HttpServletRequest request) {
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

	}*/

}
