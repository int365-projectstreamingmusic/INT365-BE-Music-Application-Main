package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.UserTrackMarkingRepository;
import com.application.services.GeneralFunctionController;

@Service
public class TrackMarkingController {

	// Track marking id in the database.
	public static int defaultMarkingPerPage = 100;
	public static int maxMarkingPerPage = 500;

	@Autowired
	private UserTrackMarkingRepository userTrackMarkingRepository;
	@Autowired
	private GeneralFunctionController generalFunctionController;

	// OK!
	// AddNewTrackMarking
	public UserTrackMarkingModel addNewTrackMarking(int trackId, int trackMarkingId, HttpServletRequest request) {
		UserAccountModel addedByUser = generalFunctionController.checkUserAccountExist(request);

		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, addedByUser.getAccountId(), trackMarkingId);
		if (userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SAVE_REJECTED, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_SAVE_REJECTED ] This record is already exist for this user.");
		}

		userTrackMarkingRepository.insertNewMarking(trackId, addedByUser.getAccountId(), trackMarkingId);
		return userTrackMarkingRepository.findTrackMarkingByCompKey(id);
	}

	// OK!
	// RemoveTrackMarking
	public void removeTrackMarking(int trackId, int trackMarkingId, HttpServletRequest request) {
		UserAccountModel removedByUser = generalFunctionController.checkUserAccountExist(request);
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, removedByUser.getAccountId(), trackMarkingId);
		if (!userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_ACCOUNT_NOT_FOUND ] This record is already gone or never be added.");
		}

		userTrackMarkingRepository.deleteById(id);
	}

	// OK!
	// ClearTrackInPlayGround
	public void clearTrackInPlayGround(HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.checkUserAccountExist(request);
		try {
			userTrackMarkingRepository.deleteAllPlaygroundById(requestedBy.getAccountId(), 1002);
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_ALREADY_GONE, HttpStatus.I_AM_A_TEAPOT,
					"[ DELETE_ALREADY_GONE ] Can't delete because the target record is not in the database.");
		}
	}

	// OK!
	// ListTrackByTrackMarkingAndUserAccountId
	public Page<UserTrackMarkingModel> listTrackByTrackMarkingAndUserAccountId(int page, int size, int trackMarkingId,
			String searchContent, HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (size < 1 || size > maxMarkingPerPage) {
			size = defaultMarkingPerPage;
		}

		Pageable pageRequest = PageRequest.of(page, size);
		Page<UserTrackMarkingModel> result;
		if (searchContent != "") {
			result = userTrackMarkingRepository.listAllFromUserIdAndTrackMarkingIdAndSearchName(1, trackMarkingId,
					pageRequest, searchContent);
		} else {
			result = userTrackMarkingRepository.listAllFromUserIdAndTrackMarkingId(1, trackMarkingId, pageRequest);
		}
		if (result.getContent().size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No record exist, found no marking for this user.");
		}

		return result;
	}

}
