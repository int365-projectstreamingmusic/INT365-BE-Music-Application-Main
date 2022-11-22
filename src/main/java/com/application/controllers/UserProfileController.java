package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.UserProfileForm;
import com.application.repositories.UserAccountRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.JwtTokenUtills;

@Service
@PropertySource("generalsetting.properties")
@PropertySource("application.properties")
public class UserProfileController {

	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private FileLinkRelController fileLinkRefController;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	@Value("${application.default.image.username}")
	private String defaultUserProfileImage;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// getMyProfile
	public UserAccountModel getMyProfile(HttpServletRequest request) {
		return generalFunctionController.getUserAccount(request);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// editBasicProfileInfo
	public UserAccountModel editBasicProfileInfo(UserProfileForm form, MultipartFile image,
			HttpServletRequest request) {
		UserAccountModel targetUser = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		if (form.getFirstName() != null && form.getFirstName() != "") {
			targetUser.setFirstName(form.getFirstName());
		}
		if (form.getLastName() != null && form.getLastName() != "") {
			targetUser.setLastName(form.getLastName());
		}
		if (form.getProfileName() != null && form.getProfileName() != "") {
			targetUser.setProfileName(form.getProfileName());
		}
		if (form.getUserBios() != null && form.getUserBios() != "") {
			targetUser.setUserBios(form.getUserBios());
		}

		if (image != null) {
			if (targetUser.getProfileIamge() != null && !targetUser.getProfileIamge().equals(defaultUserProfileImage)) {
				fileLinkRefController.deleteTargetFileByTypeIdAndLinkRef(101, targetUser.getAccountId());
			}
			String profileImageFileName = fileLinkRefController.insertNewTrackObjectLinkRel(image, 101,
					targetUser.getAccountId());
			targetUser.setProfileIamge(profileImageFileName);
		}
		targetUser = userAccountRepository.save(targetUser);
		return targetUser;
	}

}
