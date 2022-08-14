package com.application.apis;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@GetMapping("singup")
	public ResponseEntity<Map<String, Object>> userRegistration(@RequestPart UserRegiserationForm registNewUser) {
		Map<String, Object> newUser = userAuthenticationController.userRegistration(registNewUser);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/authen/singup").toString());
		return ResponseEntity.created(uri).body(newUser);
	}

	@GetMapping("login")
	public ResponseEntity<Map<String, Object>> userAuthentication(@RequestPart UserLoginForm login,
			HttpServletResponse response) {
		Map<String, Object> result = userAuthenticationController.userAuthentication(login, response);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("verify")
	public ResponseEntity<String> userVerifyAccount() {
		return null;
	}

	@GetMapping("resetPassword")
	public ResponseEntity<String> userPasswordReset() {
		return null;
	}

	@GetMapping("logoff")
	public ResponseEntity<HttpStatus> userLogOut(HttpServletResponse response) {
		return userAuthenticationController.userLogOut(response);
	}

	@GetMapping("changepassword")
	public ResponseEntity<HttpStatus> changePassword(@RequestPart ChangePasswordForm passwordForm,
			HttpServletRequest request) {
		userAuthenticationController.userChangePassword(passwordForm, request);
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/authen/changepassword").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

}
