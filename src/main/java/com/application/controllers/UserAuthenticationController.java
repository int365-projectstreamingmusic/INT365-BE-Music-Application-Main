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
import com.application.repositories.UserAccountModelRepository;
import com.application.repositories.UserRoleModelRepository;
import com.application.utilities.EmailServiceUtility;
import com.application.utilities.JwtTokenUtills;

@Service
@PropertySource("generalsetting.properties")
public class UserAuthenticationController {

	@Autowired
	private EmailServiceUtility emailServiceUtility;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private RolesRepository rolesModelRepository;
	@Autowired
	private UserRoleModelRepository userRoleModelRepository;

	@Value("${general.role.user}")
	private int userRoleMemberId;
	@Value("${general.role.unverified}")
	private int userRoleUnverfiedId;
	@Value("${general.role.suspended}")
	private int userRoleSuspendedId;

	// OK!
	// userRegistration
	public Map<String, Object> userRegistration(UserRegiserationForm incomingRegisteration) {
		UserAccountModel registeration = new UserAccountModel();

		if (userAccountModelRepository.existsByUsernameIgnoreCase(incomingRegisteration.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_USERNAME_ALREADY_EXISTED, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This usename is in use.");
		}

		if (userAccountModelRepository.existsByEmailIgnoreCase(incomingRegisteration.getEmail())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_EMAIL_ALREADY_EXIST, HttpStatus.I_AM_A_TEAPOT,
					"[ userRegistration ] This email is in use by another account.");
		}

		List<UserRolesModel> userFirstRolesGroup = new ArrayList<>();

		// Create new user
		registeration.setUser_passcode(passwordEncoder.encode(incomingRegisteration.getUser_passcode()));
		registeration.setEmail(incomingRegisteration.getEmail());
		registeration.setFirst_name("");
		registeration.setLast_name("");
		registeration.setUsername(incomingRegisteration.getUsername());
		//registeration.setProfile_name(incomingRegisteration.getUsername());
		String getRegisterationTimeStamp = new Timestamp(System.currentTimeMillis()).toString();
		registeration.setRegistered_date(getRegisterationTimeStamp);
		registeration.setUser_bios("");

		registeration = userAccountModelRepository.save(registeration);

		// Assign role number [ 1002 ] and [ 2001 ] to the newly created user.
		// Assign 1
		UserRolesModel assignUserRole1 = new UserRolesModel();
		assignUserRole1.setRoles(rolesModelRepository.findById(userRoleMemberId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ userRegistration ] This role does not exist.")));

		assignUserRole1.setUserRolesID(
				new UserRolesCompKey(registeration.getAccount_id(), assignUserRole1.getRoles().getRoles_id()));

		userFirstRolesGroup.add(assignUserRole1);

		// Assign 2
		UserRolesModel assignUserRole2 = new UserRolesModel();
		assignUserRole2.setRoles(rolesModelRepository.findById(userRoleUnverfiedId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ userRegistration ] This role does not exist.")));

		assignUserRole2.setUserRolesID(
				new UserRolesCompKey(registeration.getAccount_id(), assignUserRole2.getRoles().getRoles_id()));
		userFirstRolesGroup.add(assignUserRole2);

		userRoleModelRepository.saveAll(userFirstRolesGroup);

		// Send result when success.
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("registration", userAccountModelRepository.findById(registeration.getAccount_id()));
		resultMap.put("status", "Success");

		return resultMap;

	}

	// OK!
	// userAuthentication
	@SuppressWarnings("unlikely-arg-type")
	public Map<String, Object> userAuthentication(UserLoginForm userLoginModel, HttpServletResponse response) {
		UserAccountModel requestedUser = userAccountModelRepository.findByUsername(userLoginModel.getUserName());
		if (requestedUser == null
				|| !passwordEncoder.matches(userLoginModel.getPassword(), requestedUser.getUser_passcode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_BAD_CREDENTIALS, HttpStatus.I_AM_A_TEAPOT,
					"[ AUTHEN FAILED ] Username or password doesn't match.");
		}

		if (requestedUser.getUserRoles()
				.contains(rolesModelRepository.findById(userRoleSuspendedId).orElseThrow(
						() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
								"[ userAuthentication ] CAnnot find suspended role in the database.")))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.FORBIDDEN,
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
		result.put("token", token);
		result.put("roles", roleList);

		return result;
	}

	// OK!
	// userChangePassword
	public void userChangePassword(ChangePasswordForm passwordform, HttpServletRequest request) {
		UserAccountModel requestedUser = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (requestedUser == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ userChangePassword ] This user does not exist... seriously...?");
		}

		if (!passwordEncoder.matches(passwordform.getOldPassword(), requestedUser.getUser_passcode())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED,
					"[ userChangePassword ] User is not allowed to change a passwotd because an old password doesn't match.");
		}

		if (passwordform.getNewPassword().equals(passwordform.getConfirmationPassword())) {
			String newPasswordForRequestedUser = passwordEncoder.encode(passwordform.getNewPassword());
			userAccountModelRepository.updateUserPassword(newPasswordForRequestedUser, requestedUser.getAccount_id());
			System.out.println("At password completed");
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_PASSWORD_MISSMATCH, HttpStatus.I_AM_A_TEAPOT,
					"[ userChangePassword ] Confirmation password is not the same with new password.");
		}

	}

	// OK!
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
