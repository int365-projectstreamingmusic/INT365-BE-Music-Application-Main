package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.integration.metadata.ListenableMetadataStore;
import org.springframework.stereotype.Service;

import com.application.entities.models.PlayHistoryModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.PlayHistoryRepository;
import com.application.repositories.TracksRepository;
import com.application.repositories.UserAccountRepository;
import com.application.services.GeneralFunctionController;

@Service
public class PlayHistoryController {

	@Autowired
	private PlayHistoryRepository playHistoryRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private TrackMarkingController trackMarkingController;

	@Autowired
	private GeneralFunctionController generalFunctionController;
	@Autowired
	private TrackController trackController;

	private static int maxHistoryPageSize = 250;
	private static int defaultHistoryPageSize = 50;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// listMyLastVisit
	public List<PlayHistoryModel> listMyLastVisit(int numberOfRecord, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);
		List<PlayHistoryModel> result = playHistoryRepository.listLastVisit(requestedBy.getAccountId(), numberOfRecord);
		if (result.size() < 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This user has no history.");
		}
		return checkTrackMarking(result, requestedBy);
	}

	public List<PlayHistoryModel> checkTrackMarking(List<PlayHistoryModel> incoming, UserAccountModel user) {
		List<PlayHistoryModel> result = new ArrayList<>();
		for (int i = 0; i < incoming.size(); i++) {
			PlayHistoryModel current = incoming.get(i);
			TracksModel currentTrack = incoming.get(i).getTrack();
			currentTrack.setPlayground(
					trackMarkingController.checkIfPlayground(user.getAccountId(), incoming.get(i).getTrack().getId()));
			currentTrack.setFavorite(
					trackMarkingController.checkIfFavorite(user.getAccountId(), incoming.get(i).getTrack().getId()));
			current.setTrack(currentTrack);
			result.add(current);
		}
		return result;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// GetMyHistory
	public Page<PlayHistoryModel> getMyHistory(int page, int pageSize, String searchContent,
			HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxHistoryPageSize) {
			pageSize = defaultHistoryPageSize;
		}

		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<PlayHistoryModel> result;

		if (searchContent == "") {
			result = playHistoryRepository.listHistoryByUserId(requestedBy.getAccountId(), sendPageRequest);
		} else {
			result = playHistoryRepository.findHistoryByUserIdAndSearchName(requestedBy.getAccountId(), searchContent,
					sendPageRequest);
		}
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No record found.");
		}

		return new PageImpl<>(checkTrackMarking(result.getContent(), requestedBy), sendPageRequest,
				result.getNumberOfElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// GetRecordsByUserIdAndTrackId
	public PlayHistoryModel getRecordsByUserIdAndTrackId(int trackId, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		PlayHistoryModel playHistoryModel = playHistoryRepository
				.findRecordByUserIdAndTrackId(requestedBy.getAccountId(), trackId);
		if (playHistoryModel == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This record does not exist.");
		}
		return playHistoryModel;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// AUTOMATION METHOD
	// InsertNewHistoryByUserId
	public void InsertOrUpdateHistory(int userId, int trackId) {
		if (!(playHistoryRepository.isExistedRecord(userId, trackId) == 1)) {
			playHistoryRepository.insertNewPlayHistory(userId, trackId,
					new Timestamp(System.currentTimeMillis()).toString());
			tracksRepository.increaseViewCount(trackId);
		} else {
			playHistoryRepository.updateTimeStamp(new Timestamp(System.currentTimeMillis()), userId, trackId);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ClearHistory
	public void clearHistory(HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		if (playHistoryRepository.hasAtLeastOneRecord(requestedBy.getAccountId()) == 1) {
			playHistoryRepository.deleteAllByUserAccountId(requestedBy.getAccountId());
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ DELETE_ALREADY_GONE ] This user has no history, no need to delete anything.");
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ClearHistoryInThePassHoures
	public void clearHistoryInThePassHoures(HttpServletRequest request, int inTheLastXMinute) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		Timestamp targetAfterThisTime = new Timestamp(System.currentTimeMillis() - (inTheLastXMinute * 1000));

		if (playHistoryRepository.hasAtLeastOneRecordAfterTimeRange(requestedBy.getAccountId(),
				targetAfterThisTime) == 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ DELETE_ALREADY_GONE ] This user has no history in the past " + inTheLastXMinute
							+ " minutes, no need to delete anything.");
		} else {
			playHistoryRepository.deleteAllByUserAccountIdAndTimeRange(inTheLastXMinute, targetAfterThisTime);
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// DeleteRecordById
	public void deleteRecordById(int historyId, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		PlayHistoryModel targetHistory = playHistoryRepository.findById(historyId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ DELETE_ALREADY_GONE ] No history of this Id."));

		if (targetHistory.getAccountid() != requestedBy.getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.UNAUTHORIZED,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not the owner of this record, and is not allowed to commit change to this record.");
		} else {
			playHistoryRepository.deleteById(historyId);
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// DeleteRecordByUserIdAndTrackId
	public void deleteRecordByUserIdAndTrackId(int trackId, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		if (playHistoryRepository.isExistedRecord(requestedBy.getAccountId(), trackId) == 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ DELETE_ALREADY_GONE ] This user has no history, no need to delete anything.");
		} else {
			playHistoryRepository.deleteByUserIdAndTrackId(requestedBy.getAccountId(), trackId);
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// AUTIMATION METHOD
	// CheckAndUpdateRepeatedHistoryByUserToken
	public void checkAndUpdateRepeatedHistoryByUserToken(int trackId, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		if (playHistoryRepository.isExistedRecord(requestedBy.getAccountId(), trackId) == 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This record does not exist in the database.");
		} else {
			playHistoryRepository.updateTimeStamp(new Timestamp(System.currentTimeMillis()), requestedBy.getAccountId(),
					trackId);
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// AUTIMATION METHOD
	// CheckAndUpdateRepeatedHistoryByUserId
	public void checkAndUpdateRepeatedHistoryByUserId(int userId, int trackId) {
		if (userAccountRepository.existsByAccountId(userId) != 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ USER_ACCOUNT_NOT_FOUND ] This user does not exist in our database.");
		}

		if (playHistoryRepository.isExistedRecord(userId, trackId) == 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This record does not exist in the database.");
		} else {
			playHistoryRepository.updateTimeStamp(new Timestamp(System.currentTimeMillis()), userId, trackId);
		}

	}

}
