package com.application.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;
import com.application.entities.models.TrackMarkingModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.TrackMarkingForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TracksRepository;
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
	private TracksRepository tracksRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// CheckIfFavorite
	public boolean checkIfFavorite(int userId, int trackId) {
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, userId, 1001);
		if (userTrackMarkingRepository.existsById(id)) {
			return true;
		} else {
			return false;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// CheckIfPlayground
	public boolean checkIfPlayground(int userId, int trackId) {
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, userId, 1002);
		if (userTrackMarkingRepository.existsById(id)) {
			return true;
		} else {
			return false;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// AddNewTrackMarking
	public UserTrackMarkingModel addNewTrackMarking(int trackId, int trackMarkingId, HttpServletRequest request) {
		UserAccountModel addedByUser = generalFunctionController.getUserAccount(request);

		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, addedByUser.getAccountId(), trackMarkingId);
		if (userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SAVE_REJECTED, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_SAVE_REJECTED ] This record is already exist for this user.");
		}

		userTrackMarkingRepository.insertNewMarking(trackId, addedByUser.getAccountId(), trackMarkingId);
		return userTrackMarkingRepository.findTrackMarkingByCompKey(id);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// RemoveTrackMarking
	public void removeTrackMarking(int trackId, int trackMarkingId, HttpServletRequest request) {
		UserAccountModel removedByUser = generalFunctionController.getUserAccount(request);
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, removedByUser.getAccountId(), trackMarkingId);
		if (!userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ USER_ACCOUNT_NOT_FOUND ] This record is already gone or never be added.");
		}

		userTrackMarkingRepository.deleteById(id);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// ClearTrackInPlayGround
	public void clearTrackInPlayGround(HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);
		try {
			userTrackMarkingRepository.deleteAllPlaygroundById(requestedBy.getAccountId(), 1002);
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ DELETE_ALREADY_GONE ] Can't delete because the target record is not in the database.");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// ListTrackByTrackMarkingAndUserAccountId
	public Page<UserTrackMarkingModel> listTrackByTrackMarkingAndUserAccountId(int page, int pageSize,
			int trackMarkingId, String searchContent, HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxMarkingPerPage) {
			pageSize = defaultMarkingPerPage;
		}

		UserAccountModel user = generalFunctionController.getUserAccount(request);

		Pageable pageRequest = PageRequest.of(page, pageSize);
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
		List<UserTrackMarkingModel> finalResult = new ArrayList<>();
		if (trackMarkingId == 1001) {
			for (int i = 0; i < result.getContent().size(); i++) {
				UserTrackMarkingModel current = result.getContent().get(i);
				TracksModel currentTrack = current.getTrack();
				currentTrack.setFavorite(true);
				current.setTrack(currentTrack);
				finalResult.add(current);
			}
		} else if (trackMarkingId == 1002) {
			for (int i = 0; i < result.getContent().size(); i++) {
				UserTrackMarkingModel current = result.getContent().get(i);
				TracksModel currentTrack = current.getTrack();
				currentTrack.setPlayground(true);
				current.setTrack(currentTrack);
				finalResult.add(current);
			}
		} else {
			for (int i = 0; i < result.getContent().size(); i++) {
				UserTrackMarkingModel current = result.getContent().get(i);
				TracksModel currentTrack = current.getTrack();
				currentTrack.setFavorite(checkIfFavorite(user.getAccountId(), currentTrack.getId()));
				current.setTrack(currentTrack);
				finalResult.add(current);
			}
		}

		return new PageImpl<>(finalResult, pageRequest, result.getTotalElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// INSERT : Add many favorite list into user.
	public List<UserTrackMarkingModel> AddManyFavorite(TrackMarkingForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		if (form.getTrackId().length <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] You can't leave the track list ID blank.");
		} else {
			List<UserTrackMarkingModel> resultList = new ArrayList<>();
			for (int i = 0; i < form.getTrackId().length; i++) {
				UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(Array.getInt(form.getTrackId(), i),
						user.getAccountId(), 1001);
				if (!userTrackMarkingRepository.existsById(id) && tracksRepository.existsById(id.getTrack_id())) {
					UserTrackMarkingModel current = new UserTrackMarkingModel();
					current.setId(id);
					current = userTrackMarkingRepository.save(current);
					resultList.add(current);
				}
			}
			return resultList;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// INSERT : Add many into playground.
	public List<UserTrackMarkingModel> addManyToPlayground(TrackMarkingForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		if (form.getTrackId().length <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] You can't leave the track list ID blank.");
		} else {
			List<UserTrackMarkingModel> resultList = new ArrayList<>();
			for (int i = 0; i < form.getTrackId().length; i++) {
				UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(Array.getInt(form.getTrackId(), i),
						user.getAccountId(), 1002);
				if (!userTrackMarkingRepository.existsById(id) && tracksRepository.existsById(id.getTrack_id())) {
					UserTrackMarkingModel current = new UserTrackMarkingModel();
					current.setId(id);
					current = userTrackMarkingRepository.save(current);
					resultList.add(current);
				}
			}
			return resultList;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	//
	// DETETE : Remove many from favorite.
	public void removeManyFromFavorite(TrackMarkingForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		if (form.getTrackId().length <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] When deleting a list of favorite, You can't leave the track list ID blank.");
		} else {
			for (int i = 0; i < form.getTrackId().length; i++) {
				UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(Array.getInt(form.getTrackId(), i),
						user.getAccountId(), 1001);
				if (userTrackMarkingRepository.existsById(id)) {
					userTrackMarkingRepository.deleteById(id);
				}
			}
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	//
	// DETETE : Remove many from playground.
	public void removeManyFromPlayground(TrackMarkingForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		if (form.getTrackId().length <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] When deleting a list of favorite, You can't leave the track list ID blank.");
		} else {
			for (int i = 0; i < form.getTrackId().length; i++) {
				UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(Array.getInt(form.getTrackId(), i),
						user.getAccountId(), 1002);
				if (userTrackMarkingRepository.existsById(id)) {
					userTrackMarkingRepository.deleteById(id);
				}
			}
		}
	}
}
