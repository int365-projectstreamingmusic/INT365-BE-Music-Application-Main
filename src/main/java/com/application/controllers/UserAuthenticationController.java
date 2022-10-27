package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.UserRolesCompKey;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserRolesModel;
import com.application.entities.submittionforms.ChangePasswordForm;
import com.application.entities.submittionforms.UserLoginForm;
import com.application.entities.submittionforms.UserRegiserationForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.RolesRepository;
import com.application.repositories.UserAccountRepository;
import com.application.repositories.UserRoleModelRepository;
import com.application.utilities.EmailServiceUtility;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.ValidatorServices;

@Service
@PropertySource("generalsetting.properties")
public class UserAuthenticationController {

	@Autowired
	private EmailServiceUtility emailServiceUtility;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountRepository userAccountModelRepository;
	@Autowired
	private RolesRepository rolesModelRepository;
	@Autowired
	private UserRoleModelRepository userRoleModelRepository;
	@Autowired
	private ValidatorServices validatorServices;

	@Value("${general.role.user}")
	private int userRoleMemberId;
	@Value("${general.role.unverified}")
	private int userRoleUnverfiedId;
	@Value("${general.role.suspended}")
	private int userRoleSuspendedId;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// userRegistration
	public Map<String, Object> userRegistration(UserRegiserationForm incomingRegisteration) {
		UserAccountModel registeration = new UserAccountModel();

		if (userAccountModelRepository.existsByUsernameIgnoreCase(incomingRegisteration.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_TAKEN_USERNAME, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This usename is in use.");
		}

		if (userAccountModelRepository.existsByEmailIgnoreCase(incomingRegisteration.getEmail())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_TAKEN_EMAIL, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This email is in use by another account.");
		}

		// Validation
		if (!validatorServices.validateEmail(incomingRegisteration.getEmail())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_INVALID_EMAIL, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This is not a valid email.");
		}
		if (!validatorServices.validateUsername(incomingRegisteration.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_INVALID_USERNAME, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This username is invalid. The username must have at least 6 characters and not more than 45 characters");
		}
		if (!validatorServices.validatePassword(incomingRegisteration.getUser_passcode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_INVALID_PASSWORD, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This password has less than 8 characters and is not strong enough. The password should have capital and small letter, and a number.");
		}

		List<UserRolesModel> userFirstRolesGroup = new ArrayList<>();

		// Create new user
		registeration.setUserPasscode(passwordEncoder.encode(incomingRegisteration.getUser_passcode()));
		registeration.setEmail(incomingRegisteration.getEmail());
		registeration.setFirstName("");
		registeration.setLastName("");
		registeration.setUsername(incomingRegisteration.getUsername());
		// registeration.setProfile_name(incomingRegisteration.getUsername());
		String getRegisterationTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
		registeration.setRegistered_date(getRegisterationTimeStamp);
		registeration.setUserBios("");
		registeration.setProfileName(incomingRegisteration.getUsername());
		registeration.setProfileIamge(null);

		registeration = userAccountModelRepository.save(registeration);

		// Assign role number [ 1002 ] and [ 2001 ] to the newly created user.
		// Assign 1
		UserRolesModel assignUserRole1 = new UserRolesModel();
		assignUserRole1.setRoles(rolesModelRepository.findById(userRoleMemberId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ userRegistration ] This role does not exist.")));

		assignUserRole1
				.setId(new UserRolesCompKey(registeration.getAccountId(), assignUserRole1.getRoles().getRoles_id()));

		userFirstRolesGroup.add(assignUserRole1);

		// Assign 2
		UserRolesModel assignUserRole2 = new UserRolesModel();
		assignUserRole2.setRoles(rolesModelRepository.findById(userRoleUnverfiedId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ userRegistration ] This role does not exist.")));

		assignUserRole2
				.setId(new UserRolesCompKey(registeration.getAccountId(), assignUserRole2.getRoles().getRoles_id()));
		userFirstRolesGroup.add(assignUserRole2);

		userRoleModelRepository.saveAll(userFirstRolesGroup);

		// Send result when success.
		Map<String, Object> resultMap = new HashMap<>();
		// resultMap.put("registration",
		// userAccountModelRepository.findById(registeration.getAccountId()));
		resultMap.put("status", "Success");

		return resultMap;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// userAuthentication
	public Map<String, Object> userAuthentication(UserLoginForm userLoginModel, HttpServletResponse response) {
		UserAccountModel requestedUser = userAccountModelRepository.findByUsername(userLoginModel.getUserName());

		if (requestedUser == null
				|| !passwordEncoder.matches(userLoginModel.getPassword(), requestedUser.getUserPasscode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_INCORRECT_CREDENTIALS, HttpStatus.FORBIDDEN,
					"[ AUTHEN FAILED ] Username or password doesn't match.");
		} else if (userRoleModelRepository
				.existByUserIdAndRoleId(new UserRolesCompKey(requestedUser.getAccountId(), userRoleSuspendedId))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_ACCOUNT_SUSPENDED, HttpStatus.FORBIDDEN,
					"This account is suspended");
		}

		String[] roleList = new String[requestedUser.getUserRoles().size()];

		for (int i = 0; i <= requestedUser.getUserRoles().size() - 1; i++) {
			roleList[i] = requestedUser.getUserRoles().get(i).getRoles().getRoles();
		}

		String token = JwtTokenUtills.createToken(userLoginModel.getUserName(), roleList);
		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("UserName", userLoginModel.getUserName());
		result.put("userId", requestedUser.getAccountId());
		result.put("token", token);
		result.put("roles", roleList);

		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// userChangePassword
	public ResponseEntity<HttpStatus> userChangePassword(ChangePasswordForm passwordform, HttpServletRequest request,
			HttpServletResponse response) {
		UserAccountModel requestedUser = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (requestedUser == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_INCORRECT_CREDENTIALS, HttpStatus.UNAUTHORIZED,
					"[ userChangePassword ] This user does not exist... seriously...?");
		}

		if (!passwordEncoder.matches(passwordform.getOldPassword(), requestedUser.getUserPasscode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_INCORRECT_CREDENTIALS, HttpStatus.UNAUTHORIZED,
					"[ userChangePassword ] User is not allowed to change a passwotd because an old password doesn't match.");
		}

		if (passwordform.getNewPassword().equals(passwordform.getConfirmationPassword())) {
			String newPasswordForRequestedUser = passwordEncoder.encode(passwordform.getNewPassword());
			userAccountModelRepository.updateUserPassword(newPasswordForRequestedUser, requestedUser.getAccountId());
			return userLogOut(response);
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.REGISTERATION_PASSWORD_MISMATCH, HttpStatus.I_AM_A_TEAPOT,
					"[ userChangePassword ] Confirmation password is not the same with new password.");
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// userLogOut
	public ResponseEntity<HttpStatus> userLogOut(HttpServletResponse response) {
		response.setHeader(HttpHeaders.AUTHORIZATION, "");
		return ResponseEntity.ok().body(HttpStatus.OK);
	}

	// -------------------- WIP --------------------

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
