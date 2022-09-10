package com.application.controllers;

import java.sql.Timestamp;

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
import com.application.entities.models.ReportsModel;
import com.application.entities.models.RolesModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserRolesModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ReportTypeRepository;
import com.application.repositories.ReportsRepository;
import com.application.repositories.RolesRepository;
import com.application.repositories.UserAccountModelRepository;
import com.application.repositories.UserRoleModelRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.StringGenerateService;

@Service
@PropertySource("generalsetting.properties")
public class UserAccountManagerController {

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private RolesRepository rolesRepository;
	@Autowired
	private UserRoleModelRepository userRoleModelRepository;
	@Autowired
	private ReportsRepository reportsRepository;
	@Autowired
	private ReportTypeRepository reportTypeRepository;

	private int adminRoleNumber = 99001;
	private int suspendingRoleNumber = 2003;
	private int deletedRole = 2004;

	private int reportRypeDeleteAction = 8002;

	@Value("${general.useraccount.default-page-size}")
	private int defaultPageSize;

	@Value("${general.useraccount.max-page-size}")
	private int maxPageSize;

	// OK!
	// getProfileFromToken
	public UserAccountModel getProfileFromToken(HttpServletRequest request) {
		UserAccountModel userProfile = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		return userProfile;
	}

	// OK!
	// SwitchSuspendUser
	public boolean switchSuspendUser(int id, HttpServletRequest request) {
		boolean isSuspended;
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (!userRoleModelRepository
				.existByUserIdAndRoleId(new UserRolesCompKey(requestedBy.getAccountId(), adminRoleNumber))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ UserAccountController ] The user id " + id + " has no admin authority.");
		}

		UserAccountModel targetUser = userAccountModelRepository.findById(id)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountController ] The user with id " + id + " does not exist."));
		if (targetUser.getUsername().equals(requestedBy.getUsername())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.FORBIDDEN,
					"[ UserAccountController ] You are not allowed to suspend yourself. Why would you do that?");
		}
		if (userRoleModelRepository.existByUserIdAndRoleId(new UserRolesCompKey(id, suspendingRoleNumber))) {
			userRoleModelRepository.deleteByUserIdAndRoleId(new UserRolesCompKey(id, suspendingRoleNumber));
			isSuspended = false;
		} else {
			userRoleModelRepository.insertNewRecord(id, suspendingRoleNumber);
			isSuspended = true;
		}

		return isSuspended;
	}

	// OK!
	// AddUserRole
	public UserRolesModel addUserRole(int id, int targetRoleId, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountModelRepository.findById(id)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountController ] Can't add role to this user because the user with id " + id
								+ " does not exist."));

		RolesModel targetRole = rolesRepository.findById(targetRoleId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountController ] The role with ID " + targetRoleId + " does not exist."));

		if (userRoleModelRepository
				.existByUserIdAndRoleId(new UserRolesCompKey(targetUser.getAccountId(), targetRoleId))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_EXISTS, HttpStatus.I_AM_A_TEAPOT,
					"[ UserAccountController ] This user alrealdy have this role assigned.");
		} else if (userRoleModelRepository
				.existByUserIdAndRoleId(new UserRolesCompKey(targetUser.getAccountId(), suspendingRoleNumber))) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FORBIDDEN, HttpStatus.I_AM_A_TEAPOT,
					"[ UserAccountController ] You can't add role to the user who has been suspended.");
		}

		UserRolesModel newRole = new UserRolesModel();
		newRole.setId(new UserRolesCompKey(id, targetRoleId));
		newRole.setRoles(targetRole);
		userRoleModelRepository.save(newRole);
		return newRole;
	}

	// OK!
	// DeleteUserRole
	public void deleteUserROle(int id, int targetRoleId, HttpServletRequest request) {
		UserAccountModel targetUser = userAccountModelRepository.findById(id)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountController ] Can't add role to this user because the user with id " + id
								+ " does not exist."));

		UserRolesCompKey userRoleId = new UserRolesCompKey(targetUser.getAccountId(), targetRoleId);

		if (!userRoleModelRepository.existByUserIdAndRoleId(userRoleId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ UserAccountController ] This user does not have role id " + targetRoleId
							+ " presented in their account.");
		}

		userRoleModelRepository.deleteById(userRoleId);
	}

	// OK!
	// DeleteUserAccountRequestedByUser
	public void deleteUserAccountRequestedByUser(HttpServletRequest request) {

		String userName = JwtTokenUtills.getUserNameFromToken(request);
		UserAccountModel targetUser = userAccountModelRepository.findByUsername(userName);

		if (targetUser == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ UserAccountManagerController ] The user with the name " + userName + " does not exist.");
		}

		targetUser.setEmail(
				StringGenerateService.generateUserUUID() + "-deleted-" + StringGenerateService.generateUserUUID());
		targetUser.setUsername("Deleted-account-" + StringGenerateService.generateUserUUID());
		targetUser.setFirstName(null);
		targetUser.setLastName(null);
		targetUser.setUserPasscode("DELETED-NO-PASSWORD");
		targetUser.setLast_seen(null);
		targetUser.setUserBios(null);

		userRoleModelRepository.deleteAllByUserId(targetUser.getAccountId());
		userRoleModelRepository.insertNewRecord(targetUser.getAccountId(), deletedRole);
		userRoleModelRepository.insertNewRecord(targetUser.getAccountId(), suspendingRoleNumber);

		ReportsModel newReport = new ReportsModel();
		newReport.setIsSolved(1);
		newReport.setReportedBy(targetUser);
		newReport.setReportedToUser(targetUser);

		newReport.setType(reportTypeRepository.findById(reportRypeDeleteAction)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountManagerController ] This user can't be deleted because the report type does not exist.")));

		newReport.setReportDate(new Timestamp(System.currentTimeMillis()).toString());

		newReport.setReportText("This user deleted their account on their own on " + newReport.getReportDate());
		newReport.setSolveDate(new Timestamp(System.currentTimeMillis()).toString());

		reportsRepository.save(newReport);

	}

	// --------------- WIP ---------------

	// SearchUserByNameOrEmail
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
				throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ UserAccountManagerController ]  Found nothing here.");
			}
		}

		return result;
	}

	// ListUserByNameOrEmail
	public void listUserByNameOrEmail(String searchContent, int pageNumber, int pageSize) {

	}

}
