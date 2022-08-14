package com.application.apis;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.UserAuthenticationController;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.UserAccountModelRepository;

@RestController
@RequestMapping("api/public/")
public class PublicGeneralAPIs {

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;

	@Autowired
	private UserAuthenticationController userAuthenticationController;

	// @GetMapping("login")

	@GetMapping("username")
	public ResponseEntity<List<UserAccountModel>> getAllAccount() {
		return ResponseEntity.ok().body(userAccountModelRepository.findAll());
	}

	@GetMapping("mail/{mail}")
	public ResponseEntity<String> sendMailTest(@PathVariable(name = "mail") String mail) {
		try {
			userAuthenticationController.sendUserEmailValidation(mail);
			return ResponseEntity.ok().body("SENT!");
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	@GetMapping("exc")
	public ResponseEntity<String> testException() {
		throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.BAD_REQUEST, "F");
	}

	// Get info for the first page.
	@GetMapping("homepage")
	public ResponseEntity<String> getHomePage() {
		return ResponseEntity.ok().body("");
	}

}
