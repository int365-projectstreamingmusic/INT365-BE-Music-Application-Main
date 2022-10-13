package com.application.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.FileTypeModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.FileTypeRepository;
import com.application.repositories.TracksRepository;
import com.application.services.GeneralFunctionController;
import com.application.repositories.TrackCountRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.MinioStorageService;

import io.minio.StatObjectResponse;

@Service
@PropertySource("generalsetting.properties")
public class TrackGeneralController {
	@Value("${general.track.default-page-size}")
	private int trackDefaultSize;
	@Value("${general.track.max-page-size}")
	private int trackMaxPageSize;

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private TrackCountRepository viewCountRepository;
	@Autowired
	private FileTypeRepository fileTypeRepository;

	@Autowired
	private MinioStorageService minioStorageService;
	@Autowired
	private GeneralFunctionController generalFunctionController;

	@Value("${minio.storage.track.music}")
	String minioTrackLocation;

	@Value("${minio.storage.music-thumbnail}")
	String minioTrackThumbnailLocation;

	// OK!
	// ListLastRelease
	public List<TracksModel> listLatestRelease(int numberOfTrack) {
		List<TracksModel> result = tracksRepository.listLatsestRelease(numberOfTrack);
		if (result.size() < 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No track released today.");
		}
		return result;
	}

	// OK!
	// ListTrackByPageAndName
	public Page<TracksModel> listTrackByPageAndName(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> result;

		if (searchContent == "") {
			result = tracksRepository.findAll(sendPageRequest);
		} else {
			result = tracksRepository.findByTrackName(searchContent, sendPageRequest);
			if (result.getTotalPages() < page + 1) {
				throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ TrackController ] Found nothing here.");
			}
		}

		return result;
	}

	// OK!
	// GetTrackDetailById
	public Map<String, Object> getTrackDetailById(int trackId) {

		TracksModel resultTrack = tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ TrackController ] The track id " + trackId + " doees not exist."));

		int totalViewCount = viewCountRepository.getAllViewCountFromTrackId(trackId);

		FileTypeModel trackFile = fileTypeRepository.findById(1001).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
				"[ TrackController ] A type for this trackfile does not exist. This is because the record in the database is missing."));

		StatObjectResponse trackFileObject = minioStorageService
				.getStatObjectFromObject(trackFile.getPathRel() + resultTrack.getTrackFile());

		Map<String, Object> result = new HashMap<>();
		result.put("trackDetail", resultTrack);
		result.put("totalViewCount", totalViewCount);
		result.put("contentType", trackFileObject.contentType());
		result.put("contentSize", trackFileObject.size());

		return result;
	}

	// ------------------------------ WIP ------------------------------

	// ListTrackByPageAndArtist
	public Page<TracksModel> listTrackByPageAndArtist(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> result;

		if (searchContent == "") {

		} else {

		}
		return null;
	}

	// ListTrackOfTheWeek
	public List<TracksModel> listTrackOfTheWeek() {
		return null;
	}

	// ListNewArrivalTrack
	public List<TracksModel> listNewArrivalTrack() {
		return null;
	}

	// ----------------- WIP --------------------

	// Put track into playground.
	public void addTrackToPlayground(int trackId, HttpServletRequest request) {
		String addedBy = JwtTokenUtills.getUserNameFromToken(request);

		UserTrackMarkingModel newUserMarking = new UserTrackMarkingModel();
		newUserMarking.setId(null);

	}

	// Put track into specific playlist.

	// Remove track from playground.

	// Remove track from playlist.

	// Favorite track

	// Unlike track

	// Get all tracks in the playlist by page.

	// Get all favorite track by page.

	// Get all track from playground by page.

	// Report Track

	// ListMyTrack
	public Page<TracksModel> ListMyTrack(int page, int pageSize, String searchContent, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(00, 0);
		Page<TracksModel> result;

		if (searchContent == "") {
			result = tracksRepository.listMyTrack(requestedBy.getAccountId(), sendPageRequest);
		} else {
			result = tracksRepository.listMyTrackByName(requestedBy.getAccountId(), searchContent, sendPageRequest);
		}

		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] THis user has no owned track.");
		}

		return result;
	}

	// ChangeTrackStatus
	public void ChangeTrackStatus(int trackId, int trackStatusId, HttpServletRequest request) {

	}

	// DeleteTrack
	public void deleteTrack(int trackId, HttpServletRequest request) {

	}

}
