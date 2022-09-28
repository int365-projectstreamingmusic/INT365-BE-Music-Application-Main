package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TrackMarkingRepository;
import com.application.repositories.UserAccountRepository;
import com.application.repositories.UserTrackMarkingRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class TrackMarkingController {

	// Track marking id in the database.
	private int markingFavorite = 1001;
	private int markingPlaylist = 1002;

	@Autowired
	private UserTrackMarkingRepository userTrackMarkingRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;
	@Autowired
	private JwtTokenUtills jwtTokenUtills;

	// OK!
	// AddFavoriteTrack
	public UserTrackMarkingModel addFavoriteTrack(int trackId, HttpServletRequest request) {
		UserAccountModel addedByUser = jwtTokenUtills.getUserAccountFromToken(request);
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, addedByUser.getAccountId(), markingFavorite);
		if (userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.I_AM_A_TEAPOT,
					"[ REJECTED ] This record is already exist for this user.");
		}
		userTrackMarkingRepository.insertNewMarking(trackId, addedByUser.getAccountId(), markingFavorite);
		return userTrackMarkingRepository.findTrackMarkingByCompKey(id);
	}

	// OK!
	// RemoveFavoriteTrack
	public void removeFavoriteTrack(int trackId, HttpServletRequest request) {
		UserAccountModel removedByUser = jwtTokenUtills.getUserAccountFromToken(request);
		UserTrackMarkingCompkey id = new UserTrackMarkingCompkey(trackId, removedByUser.getAccountId(), markingFavorite);
		if(!userTrackMarkingRepository.existsById(id)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SEARCH_NOT_FOUND, HttpStatus.I_AM_A_TEAPOT,
					"[ REJECTED ] This record is already gone or never be added.");
		}
		userTrackMarkingRepository.deleteById(id);
	}

	// AddTrackToPlayGround
	public void addTrackToPlayGround(int trackId, HttpServletRequest request) {

	}

	// RemoveTrackFromPlayGround
	public void removeTrackFromPlayGround(int trackId, HttpServletRequest request) {

	}

	// ClearTrackInPlayGround
	public void clearTrackInPlayGround(HttpServletRequest request) {

	}

	// ListFavoriteTrack
	public void listFavoriteTrack(HttpServletRequest request) {

	}

	// ListTrackInPlayGround
	public void listTrackInPlayGround(HttpServletRequest request) {

	}

}
