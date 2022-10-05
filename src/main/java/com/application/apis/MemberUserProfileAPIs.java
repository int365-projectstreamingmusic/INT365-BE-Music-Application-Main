package com.application.apis;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.UserAccountController;
import com.application.controllers.UserProfileController;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.UserProfileForm;

@RestController
@RequestMapping("api/profile/")
public class MemberUserProfileAPIs {

	@Autowired
	private UserProfileController userProfileController;
	@Autowired
	private UserAccountController userAccountManagerController;

	// OK!
	// getMyProfile
	@GetMapping("myprofile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest servletRequest) {
		return ResponseEntity.ok().body(userAccountManagerController.getProfileFromToken(servletRequest));
	}

	// OK!
	// editMyBios
	@PutMapping("user-bio")
	public ResponseEntity<String> editMyBio(@RequestBody String NewBio, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/user-bio").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewBio(NewBio, request));
	}

	// OK!
	// EditFirstName
	@PutMapping("first-name")
	public ResponseEntity<String> editMyFirstName(@RequestBody String newFirstName, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/first-name").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewFirstName(newFirstName, request));
	}

	// OK!
	// EditLastName
	@PutMapping("last-name")
	public ResponseEntity<String> editMyLastName(@RequestBody String newLastName, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/last-name").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewLastName(newLastName, request));
	}

	// OK!
	// EditProfileName
	@PutMapping("profile-name")
	public ResponseEntity<String> editMyProfile(@RequestBody String newProfileName, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/profile-name").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewProfileName(newProfileName, request));
	}

	// OK!
	// EditBasicProfileInfo
	@PutMapping("edit-profile-info")
	public ResponseEntity<UserAccountModel> editBasicProfileInfo(@RequestBody UserProfileForm newProfileInfo,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/edit-profile-info").toString());
		return ResponseEntity.created(uri).body(userProfileController.editBasicProfileInfo(newProfileInfo, request));
	}

	// OK!
	// updateProfileImage
	@PutMapping("profile-image")
	public ResponseEntity<String> updateProfileImage(@RequestPart MultipartFile profileImage,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/profile-image").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewUserProfileImage(profileImage, request));
	}

}
