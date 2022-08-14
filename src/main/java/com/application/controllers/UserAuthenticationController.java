package com.application.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserRolesModel;
import com.application.entities.submittionforms.ChangePasswordForm;
import com.application.entities.submittionforms.UserLoginForm;
import com.application.entities.submittionforms.UserRegiserationForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.RolesModelRepository;
import com.application.repositories.UserAccountModelRepository;
import com.application.utilities.EmailServiceUtility;
import com.application.utilities.JwtTokenUtills;

@Service
public class UserAuthenticationController {

	@Autowired
	private EmailServiceUtility emailServiceUtility;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private RolesModelRepository rolesModelRepository;

	// userRegistration
	public Map<String, Object> userRegistration(UserRegiserationForm incomingRegisteration) {
		UserAccountModel registeration = new UserAccountModel();

		if (userAccountModelRepository.existsByFirst_NameIgnoreCaseAndExistsByLast_NameIgnoreCase(
				incomingRegisteration.getFirst_name(), incomingRegisteration.getLast_name())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_USERNAME_ALREADY_EXISTED, HttpStatus.NOT_ACCEPTABLE,
					"[ userRegistration ] This firstname and lastname is in use. You can have the same first name or last name but not both!");
		}

		if (userAccountModelRepository.existsByUsernameIgnoreCase(incomingRegisteration.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_USERNAME_ALREADY_EXISTED, HttpStatus.NOT_ACCEPTABLE,
					"[ userRegistration ] This usename is in use.");
		}

		if (userAccountModelRepository.existsByEmailIgnoreCase(incomingRegisteration.getEmail())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_EMAIL_ALREADY_EXIST, HttpStatus.NOT_ACCEPTABLE,
					"[ userRegistration ] This email is in use by another account.");
		}

		registeration.setUser_passcode(passwordEncoder.encode(incomingRegisteration.getUser_passcode()));
		registeration.setEmail(incomingRegisteration.getEmail());
		registeration.setFirst_name(incomingRegisteration.getFirst_name());
		registeration.setLast_name(incomingRegisteration.getLast_name());
		registeration.setUsername(incomingRegisteration.getUsername());
		registeration.setUser_bios("");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("registeration", registeration);
		resultMap.put("status", "Success");

		userAccountModelRepository.save(registeration);

		return resultMap;

	}

	// userAuthentication
	@SuppressWarnings("unlikely-arg-type")
	public Map<String, Object> userAuthentication(UserLoginForm userLoginModel, HttpServletResponse response) {
		UserAccountModel requestedUser = userAccountModelRepository.findByUsername(userLoginModel.getUserName());
		if (requestedUser == null
				|| !passwordEncoder.matches(userLoginModel.getPassword(), requestedUser.getUser_passcode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_BAD_CREDENTIALS, HttpStatus.NOT_ACCEPTABLE,
					"[ AUTHEN FAILED ] Username or password doesn't match.");
		}

		if (requestedUser.getUserRoles()
				.contains(rolesModelRepository.findById(3001).orElseThrow(() -> new ExceptionFoundation(
						EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ userAuthentication ] The role ID " + 3001 + " or \" suspended user \" does not exit")))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.FORBIDDEN,
					"This account is suspended");
		}

		String[] roleList = { "" };

		for (int i = 0; i < requestedUser.getUserRoles().size(); i++) {
			roleList[i] = requestedUser.getUserRoles().get(i).getRoles().getRoles();
		}

		String token = JwtTokenUtills.createToken(userLoginModel.getUserName(), roleList);
		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("UserName", result);
		result.put("token", token);
		result.put("roles", roleList);

		return result;
	}

	// userChangePassword
	public void userChangePassword(ChangePasswordForm passwordform,
			HttpServletRequest request) {
		UserAccountModel requestedUser = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (requestedUser == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.BAD_REQUEST,
					"[ userChangePassword ] This user does not exist... seriously...?");
		}

		if (passwordEncoder.matches(passwordform.getOldPassword(), requestedUser.getUser_passcode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED,
					"[ userChangePassword ] User is not allowed to change a passwotd because an old password doesn't match.");
		}

		if (passwordform.getNewPassword().equals(passwordform.getConfirmationPassword())) {
			String newPasswordForRequestedUser = passwordEncoder.encode(passwordform.getNewPassword());
			//ajfow;afnwaoif
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_PASSWORD_MISSMATCH, null,
					"[ userChangePassword ] Confirmation password is not the same with new password.");
		}

	}

	// userLogOut
	public ResponseEntity<HttpStatus> userLogOut(HttpServletResponse response) {
		response.setHeader(HttpHeaders.AUTHORIZATION, "");
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	// sendUserEmailValidation
	public void sendUserEmailValidation(String recieverEmail) throws MessagingException {
		emailServiceUtility.sendHtmlRmail(recieverEmail);
	}
	
	// userVerifyAccount
	public void userVerifyAccount(String verificationCode) {

	}

	// userPasswordResetRequest
	public void userPasswordResetRequest(String userEmailAddress) {

	}

	// userPasswordResetCommit
	public void userPasswordResetCommit(String passwordResetCode) {

	}
}
