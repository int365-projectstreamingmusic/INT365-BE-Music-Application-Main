package com.application.apis;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.UserAccountManagerController;
import com.application.entities.models.UserAccountModel;

@RestController
@RequestMapping("api/profile/")
public class MemberUserProfileAPIs {

	@Autowired
	private UserAccountManagerController userAccountController;

	// OK!
	// getMyProfile
	@GetMapping("myprofile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest servletRequest) {
		return ResponseEntity.ok().body(userAccountController.getProfileFromToken(servletRequest));
	}

	// editMyBios
	@PutMapping("bio")
	public ResponseEntity<String> editMyBio(@RequestBody String NewBio, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/first-name").toString());
		return ResponseEntity.created(uri).body("");
	}

	// EditFirstName
	@PutMapping("first-name")
	public ResponseEntity<String> editMyFirstName(@RequestBody String newFirstName, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/first-name").toString());
		return ResponseEntity.created(uri).body("");
	}
	
	// EditLastName
	@PutMapping("last-name")
	public ResponseEntity<String> editMyLastName(@RequestBody String newFirstName, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/first-name").toString());
		return ResponseEntity.created(uri).body("");
	}

}
