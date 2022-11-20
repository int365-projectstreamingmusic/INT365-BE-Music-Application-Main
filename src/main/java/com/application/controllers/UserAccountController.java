package com.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.UserRolesCompKey;
import com.application.entities.models.RolesModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserRolesModel;
import com.application.entities.submittionforms.ActionForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.RolesRepository;
import com.application.repositories.UserAccountRepository;
import com.application.repositories.UserRoleModelRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.JwtTokenUtills;

@Service
@PropertySource("generalsetting.properties")
public class UserAccountController {

	@Autowired
	private UserAccountRepository userAccountModelRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private UserRoleModelRepository userRoleModelRepository;

	@Autowired
	private ActionHistoryController actionHistoryController;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	@Value("${general.useraccount.default-page-size}")
	private int defaultPageSize;

	@Value("${general.useraccount.max-page-size}")
	private int maxPageSize;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Assign a single role from user.
	// EXCEPTION | 11001 | ROLE_NOT_FOUND
	// EXCEPTION | 11002 | ROLE_INSUFFICIENT_PRIVILEGE
	// EXCEPTION | 11003 | ROLE_FORBIDDEN_STRENGTH
	// EXCEPTION | 11004 | ROLE_ALREADY_EXIST
	// EXCEPTION | 12002 | USER_SEARCH_NOT_FOUND
	public String grantRole(int userId, int roleId, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		UserAccountModel targetUser = userAccountModelRepository.findById(userId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ USER_SEARCH_NOT_FOUND ] The user with this ID does not exist."));
		RolesModel targetRole = rolesRepository.findById(roleId).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND,
				"[ ROLE_NOT_FOUND ] Role that you are looking for does not exist with this ID. Please see the /api/public/listRole for available roles. "));

		int userRoleStrength = isAllowedToManageRole(user.getUserRoles(), targetUser.getUserRoles());

		if (targetRole.getRoleStrength() >= userRoleStrength) {
			throw new ExceptionFoundation(EXCEPTION_CODES.ROLE_FORBIDDEN_STRENGTH, HttpStatus.I_AM_A_TEAPOT,
					"[ ROLE_FORBIDDEN_STRENGTH ] You can't add or remove a role to or from the user with similar or higher strength. ( As know as a priority. ) YOu need a person with higher rank do that for you.");
		} else if (userRoleModelRepository.existsById(new UserRolesCompKey(userId, roleId))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.ROLE_ALREADY_EXIST, HttpStatus.I_AM_A_TEAPOT,
					"[ ROLE_ALREADY_EXIST ] The user already has this role.");
		}

		String successMessage = "[ Role added ] The user " + user.getUsername() + " added role '"
				+ targetRole.getRoles() + " " + targetRole.getRoles_id() + "' to '" + targetUser.getUsername() + "'.";
		userRoleModelRepository.addRoleToUser(userId, roleId);
		actionHistoryController.addNewRecord(new ActionForm(user, targetUser.getAccountId(), 105, successMessage));
		return successMessage;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Remove a single role from user.
	// EXCEPTION | 11001 | ROLE_NOT_FOUND
	// EXCEPTION | 11002 | ROLE_INSUFFICIENT_PRIVILEGE
	// EXCEPTION | 11003 | ROLE_FORBIDDEN_STRENGTH
	// EXCEPTION | 11005 | ROLE_ALREADY_GONE
	// EXCEPTION | 11006 | ROLE_FORBIDDEN
	// EXCEPTION | 12002 | USER_SEARCH_NOT_FOUND
	public String revokeRole(int userId, int roleId, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		UserAccountModel targetUser = userAccountModelRepository.findById(userId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ USER_SEARCH_NOT_FOUND ] The user with this ID does not exist."));

		RolesModel targetRole = rolesRepository.findById(roleId).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.ROLE_NOT_FOUND, HttpStatus.NOT_FOUND,
				"[ ROLE_NOT_FOUND ] Role that you are looking for does not exist with this ID. Please see the /api/public/listRole for available roles. "));

		isAllowedToManageRole(user.getUserRoles(), targetUser.getUserRoles());
		if (!userRoleModelRepository.existsById(new UserRolesCompKey(userId, roleId))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.ROLE_ALREADY_GONE, HttpStatus.I_AM_A_TEAPOT, "");
		}
		String successMessage = "[ Role removed ] The user " + user.getUsername() + " removed role '"
				+ targetRole.getRoles() + " " + targetRole.getRoles_id() + "' from '" + targetUser.getUsername() + "'.";
		userRoleModelRepository.removeRoleFromUser(userId, roleId);
		actionHistoryController.addNewRecord(new ActionForm(user, targetUser.getAccountId(), 105, successMessage));
		return successMessage;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// SwitchSuspendUser
	// NOTE : If the user is suspended, delete the suspended role after used this
	// function.
	// EXCEPTION | 12002 | USER_SEARCH_NOT_FOUND
	// EXCELTION | 12006 | USER_NO_SELF_SUSPEND
	public String switchSuspendUser(int userId, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		UserAccountModel targetUser = userAccountModelRepository.findById(userId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ USER_SEARCH_NOT_FOUND ] The user with id " + userId + " does not exist."));

		isAllowedToManageRole(requestedBy.getUserRoles(), targetUser.getUserRoles());
		if (targetUser.getUsername().equals(requestedBy.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_NO_SELF_SUSPEND, HttpStatus.FORBIDDEN,
					"[ USER_NO_SELF_SUSPEND ] You are not allowed to suspend yourself. Why would you do that?");
		}

		String successMessage = "";
		if (userRoleModelRepository.existByUserIdAndRoleId(new UserRolesCompKey(userId, 2003))) {
			userRoleModelRepository.deleteByUserIdAndRoleId(new UserRolesCompKey(userId, 2003));
			successMessage = "[ Unsuspended ] The staff " + requestedBy.getUsername() + " unsuspended the user "
					+ targetUser.getUsername() + ".";
			actionHistoryController
					.addNewRecord(new ActionForm(requestedBy, targetUser.getAccountId(), 104, successMessage));
		} else {
			userRoleModelRepository.insertNewRecord(userId, 2003);
			successMessage = "[ Suspended ] The staff " + requestedBy.getUsername() + " suspended the user "
					+ targetUser.getUsername() + ".";
			actionHistoryController
					.addNewRecord(new ActionForm(requestedBy, targetUser.getAccountId(), 104, successMessage));
		}
		return successMessage;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Check if allowed
	private int isAllowedToManageRole(List<UserRolesModel> requestedUserRoleList,
			List<UserRolesModel> targetUserRoleList) {
		int requestedUserStrength = requestedUserRoleList.get(0).getRoles().getRoleStrength();
		int targetUserStrength = targetUserRoleList.get(0).getRoles().getRoleStrength();

		if (requestedUserRoleList.size() > 1) {
			for (int i = 1; i < requestedUserRoleList.size(); i++) {
				int currentStrength = requestedUserRoleList.get(i).getRoles().getRoleStrength();
				if (currentStrength > requestedUserStrength) {
					requestedUserStrength = currentStrength;
				}
			}
		}

		if (targetUserRoleList.size() > 1) {
			for (int i = 1; i < targetUserRoleList.size(); i++) {
				int currentStrength = targetUserRoleList.get(i).getRoles().getRoleStrength();
				if (currentStrength > targetUserStrength) {
					targetUserStrength = currentStrength;
				}
			}
		}

		if (requestedUserStrength > targetUserStrength) {
			return requestedUserStrength;
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.ROLE_INSUFFICIENT_PRIVILEGE, HttpStatus.I_AM_A_TEAPOT,
					"[ ROLE_INSUFFICIENT_PRIVILEGE ] You can't assign or remove role to or from the user with higher ranks.");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// NOTE : Once user logged in, will get their own profile information.
	// getProfileFromToken
	public UserAccountModel getProfileFromToken(HttpServletRequest request) {
		UserAccountModel userProfile = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		return userProfile;
	}

	// SearchUserByNameOrEmail
	// NOTE : List all user in the application.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Page<UserAccountModel> searchUserByNameOrEmail(String searchContent, int pageNumber, int pageSize) {
		if (pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize < 1 || pageSize > maxPageSize) {
			pageSize = defaultPageSize;
		}

		Pageable sendPageRequest = PageRequest.of(pageNumber, pageSize);
		Page<UserAccountModel> result;

		if (searchContent == "") {
			result = userAccountModelRepository.findAll(sendPageRequest);
		} else {
			result = userAccountModelRepository.findByUsernameContainingOrEmailContaining(searchContent,
					sendPageRequest);
			if (result.getTotalPages() < pageNumber + 1) {
				throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ]  No user found.");
			}
		}
		return result;
	}
}
