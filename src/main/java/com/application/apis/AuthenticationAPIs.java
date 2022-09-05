package com.application.apis;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.UserAuthenticationController;
import com.application.entities.submittionforms.ChangePasswordForm;
import com.application.entities.submittionforms.UserLoginForm;
import com.application.entities.submittionforms.UserRegiserationForm;

@RestController
@RequestMapping("api/authen/")
public class AuthenticationAPIs {

	@Autowired
	UserAuthenticationController userAuthenticationController;


//OK!
	// UserRegistration
	@PostMapping("signup")
	public ResponseEntity<Map<String, Object>> userRegistration(@RequestPart UserRegiserationForm registerNewUser) {
		Map<String, Object> newUser = userAuthenticationController.userRegistration(registerNewUser);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/authen/signup").toString());
		return ResponseEntity.created(uri).body(newUser);
	}

	//OK!
	// UserAuthentication
	@GetMapping("login")
	public ResponseEntity<Map<String, Object>> userAuthentication(@RequestPart UserLoginForm login,
			HttpServletResponse response) {
		Map<String, Object> result = userAuthenticationController.userAuthentication(login, response);
		return ResponseEntity.ok().body(result);
	}

	//OK!
	// UserLogOut
	@GetMapping("logout")
	public ResponseEntity<HttpStatus> userLogOut(HttpServletResponse response) {
		return userAuthenticationController.userLogOut(response);
	}

	//OK!
	// UserChangePassword
	@PutMapping("changepassword")
	public ResponseEntity<HttpStatus> userChangePassword(@RequestPart ChangePasswordForm passwordForm,
			HttpServletRequest request) {
		userAuthenticationController.userChangePassword(passwordForm, request);
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/authen/changepassword").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// --------------------- WIP ---------------------

	// UserVerifyAccount
	@GetMapping("verify")
	public ResponseEntity<String> userVerifyAccount() {
		return null;
	}

	// UserPasswordReset
	@GetMapping("resetPassword")
	public ResponseEntity<String> userPasswordReset() {
		return null;
	}

}
