package com.application.controllers;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.TrackForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.PlayTrackStatusRepository;
import com.application.repositories.TracksRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.MinioStorageService;

@Service
public class TrackController {

	@Value("${general.track.default-page-size}")
	private int trackDefaultSize;
	@Value("${general.track.max-page-size}")
	private int trackMaxPageSize;

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private PlayTrackStatusRepository playTrackStatusRepository;

	@Autowired
	private GenreController genreController;
	@Autowired
	private TrackMarkingController trackMarkingController;

	@Autowired
	private MinioStorageService minioStorageService;
	@Autowired
	private FileLinkRelController fileLinkRelController;
	@Autowired
	private GeneralFunctionController generalFunctionController;
	@Autowired
	private TrackStatisticController trackCountController;
	@Autowired
	private MoodController moodController;

	@Value("${minio.storage.track.music}")
	String minioTrackLocation;

	@Value("${minio.storage.music-thumbnail}")
	String minioTrackThumbnailLocation;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ListTrackByPageAndName
	public Page<TracksModel> listTrackByPageAndName(int page, int pageSize, String searchContent,
			HttpServletRequest request) {

		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> result;

		// Chech if they were looking for something.
		if (searchContent == "") {
			result = tracksRepository.findAll(sendPageRequest);
		} else {
			result = tracksRepository.findByTrackName(searchContent, sendPageRequest);
			if (result.getTotalPages() < page + 1) {
				throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ TrackController ] Found nothing here. Seems like there is no track here.");
			}
		}
		// If logged in, search for user' favorite. If not, will just send a result.
		return new PageImpl<>(getFavoriteTrackList(result.stream().collect(Collectors.toList()), request),
				sendPageRequest, result.getTotalElements());

	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
	//
	// Visitor zone for creator only.
	//
	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// getTrackById
	public TracksModel getTrackById(int trackId, HttpServletRequest request) {
		TracksModel track = tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));
		if (track.getPlayTrackStatus().getId() != 1001) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_FORBIDDEN, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_FORBIDDEN ] This track is not visible to public.");
		}
		// If logged in, search for user' favorite. If not, will just send a result.
		try {
			UserAccountModel userAccount = generalFunctionController.getUserAccount(request);
			track.setFavorite(trackMarkingController.checkIfFavorite(userAccount.getAccountId(), trackId));
			return track;
		} catch (Exception exc) {
			return track;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// listLatestRelease
	public List<TracksModel> listLatestRelease(int numberOfTracks, HttpServletRequest request) {
		if (numberOfTracks > trackMaxPageSize) {
			numberOfTracks = trackMaxPageSize;
		}
		List<TracksModel> result = tracksRepository.listLatsestRelease(numberOfTracks);
		if (result.size() < 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No track released today.");
		}
		// If logged in, search for user' favorite. If not, will just send a result.
		return getFavoriteTrackList(result, request);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// getTopTrack
	public List<TracksModel> getTopTrackOfNDay(int numberOfTracks, HttpServletRequest request) {
		if (numberOfTracks > trackMaxPageSize) {
			numberOfTracks = trackMaxPageSize;
		}
		List<TracksModel> result = tracksRepository.listTopTrack(numberOfTracks);
		return getFavoriteTrackList(result, request);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// getTopTrackOfNDay
	public List<TracksModel> getTopTrackOfNDay(int numberOfTracks, int lastDay, HttpServletRequest request) {
		if (numberOfTracks > trackMaxPageSize) {
			numberOfTracks = trackMaxPageSize;
		}
		long timestampToday = trackCountController.getTimestampToday().getTime();

		String from = trackCountController
				.getTimeStampFromMilisecond(timestampToday - (trackCountController.TIME_DIF_DAY * lastDay)).toString();
		String to = trackCountController.getTimeStampFromMilisecond(timestampToday).toString();

		List<TracksModel> result = tracksRepository.listTopTrack(numberOfTracks, from, to);
		return getFavoriteTrackList(result, request);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get tracks in albums.
	public Page<TracksModel> listTrackByAlbum(int albumId, int page, int pageSize, String searchContent,
			HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> result;

		result = tracksRepository.listAllByAlbum(albumId, searchContent, sendPageRequest);
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ TrackController ] Found nothing here. Seems like there is no track here.");
		}
		return new PageImpl<>(getFavoriteTrackList(result.stream().collect(Collectors.toList()), request),
				sendPageRequest, result.getTotalElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get list of track in playlist
	public Page<TracksModel> listTrackByPlaylist(int playlistId, int page, int pageSize, String searchContent,
			boolean isByPlaylistOwner, HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> result;
		if (isByPlaylistOwner) {
			result = tracksRepository.listAllByPlaylistOwner(playlistId, searchContent, sendPageRequest);
		} else {
			result = tracksRepository.listAllByPlaylist(playlistId, searchContent, sendPageRequest);
		}
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ TrackController ] Found nothing here. Seems like there is no track here.");
		}
		return new PageImpl<>(getFavoriteTrackList(result.stream().collect(Collectors.toList()), request),
				sendPageRequest, result.getTotalElements());
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
	//
	// Management zone for creator only.
	//
	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// AddNewTrack
	public TracksModel addNewTrack(TrackForm newTrackForm, MultipartFile trackFile, MultipartFile imageFile,
			HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		TracksModel newTrack = new TracksModel();
		// General information.
		newTrack.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		newTrack.setAccountId(requestedBy.getAccountId());

		// Information given in the form.
		newTrack.setTrackDesc(newTrackForm.getTrackDesc());
		newTrack.setTrackName(newTrackForm.getTrackName());

		// No view or favorite at the begining.
		newTrack.setViewCount(0);
		newTrack.setFavoriteCount(0);
		newTrack.setDuration(0);

		// Track status is hiden when first uploaded.
		newTrack.setPlayTrackStatus(playTrackStatusRepository.findById(newTrackForm.getStatusId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID, HttpStatus.I_AM_A_TEAPOT,
						"[ SAVE_FILE_INVALID ] Invalid track status ID.")));

		// Thinks to add later.
		newTrack.setGenreTrack(null);
		newTrack.setMoods(null);
		newTrack.setTrackFile("-");
		newTrack = tracksRepository.save(newTrack);

		// Adding genre to the track
		if (newTrackForm.getGenreList() != null && !(newTrackForm.getGenreList().size() <= 0)) {
			newTrack.setGenreTrack(genreController.addGenreToTrack(newTrack.getId(), newTrackForm.getGenreList()));
		}
		if (newTrackForm.getMoodList() != null && !(newTrackForm.getMoodList().size() <= 0)) {
			newTrack.setMoods(moodController.addMoodToTrack(newTrack.getId(), newTrackForm.getMoodList()));
		}

		// Save image and track file.
		if (imageFile != null) {
			String trackThumbnailFileName = fileLinkRelController.insertNewTrackObjectLinkRel(imageFile, 201,
					newTrack.getId());
			newTrack.setTrackThumbnail(trackThumbnailFileName);
			tracksRepository.updateTrackThumbnail(newTrack.getId(), trackThumbnailFileName);
		}
		String uploadedTrack = minioStorageService.uploadTrackToStorage(trackFile, minioTrackLocation);
		newTrack.setTrackFile(uploadedTrack);
		tracksRepository.updateTrackFileName(uploadedTrack, newTrack.getId());

		TracksModel result = tracksRepository.findById(newTrack.getId()).orElse(null);
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// editTrack
	public TracksModel editTrack(TrackForm trackInfo, MultipartFile image, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);
		TracksModel target = tracksRepository.findById(trackInfo.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));

		generalFunctionController.checkOwnerShipForRecord(requestedBy.getAccountId(), target.getAccountId());
		if (trackInfo.getTrackName() != "") {
			target.setTrackName(trackInfo.getTrackName());
		}
		if (trackInfo.getTrackDesc() != "") {
			target.setTrackDesc(trackInfo.getTrackDesc());
		}
		tracksRepository.updateBasicTrackInfo(target.getId(), target.getTrackName(), target.getTrackDesc());

		if (trackInfo.getGenreList() != null) {
			target.setGenreTrack(genreController.addGenreToTrack(target.getId(), trackInfo.getGenreList()));
		}

		// If with image, do the following.
		if (image != null) {
			if (fileLinkRelController.isExistsInRecord(target.getTrackThumbnail())) {
				fileLinkRelController.deleteTargetFileByName(target.getTrackThumbnail());
			}
			String trackThumbnailFileName = fileLinkRelController.insertNewTrackObjectLinkRel(image, 201,
					target.getId());
			tracksRepository.updateTrackThumbnail(target.getId(), trackThumbnailFileName);
		}
		return target;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// SwitchTrackStatus
	public String switchTrackStatus(int trackId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		TracksModel target = tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), target.getAccountId());

		if (target.getPlayTrackStatus().getId() == 1001) {
			tracksRepository.updateTrackStatus(trackId, 1002);
			return "The track ID " + trackId + ":" + target.getTrackName() + " is now hiden.";
		} else if (target.getPlayTrackStatus().getId() == 1002) {
			tracksRepository.updateTrackStatus(trackId, 1001);
			return "The track ID " + trackId + ":" + target.getTrackName() + " is now visible.";
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SAVE_REJECTED, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_SAVE_REJECTED ] This playlise is marked as removed, or in breach of the agreement.");
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// DeleteTrack
	public void deleteTrack(int trackId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		TracksModel target = tracksRepository.findById(trackId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist. Nothing is deleted."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), target.getAccountId());
		tracksRepository.deleteById(trackId);
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
	//
	// Automation - No API
	//
	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// getFavoriteTrackList
	public List<TracksModel> getFavoriteTrackList(List<TracksModel> incomingList, HttpServletRequest request) {
		try {
			UserAccountModel userAccount = generalFunctionController.getUserAccount(request);
			for (int i = 0; i < incomingList.size(); i++) {
				if (trackMarkingController.checkIfFavorite(userAccount.getAccountId(), incomingList.get(i).getId())) {
					TracksModel currentTrackCheck = incomingList.get(i);
					currentTrackCheck.setFavorite(true);
					incomingList.set(i, currentTrackCheck);
				}
			}
			return incomingList;
		} catch (Exception e) {
			return incomingList;
		}
	}
}
