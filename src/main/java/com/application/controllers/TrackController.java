package com.application.controllers;

import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.TrackForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.PlayTrackRepository;
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
	private PlayTrackRepository playTrackRepository;

	@Autowired
	private GenreController genreController;

	@Autowired
	private MinioStorageService minioStorageService;
	@Autowired
	private FileLinkRelController fileLinkRelController;
	@Autowired
	private GeneralFunctionController generalFunctionController;

	@Value("${minio.storage.track.music}")
	String minioTrackLocation;

	@Value("${minio.storage.music-thumbnail}")
	String minioTrackThumbnailLocation;

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
		newTrack.setPlayTrackStatus(playTrackRepository.findById(1002).get());

		// Thinks to add later.
		newTrack.setGenreTrack(null);
		newTrack.setTrackFile("-");
		newTrack = tracksRepository.save(newTrack);

		// Adding genre to the track
		if (newTrackForm.getGenreList() != null) {
			newTrack.setGenreTrack(genreController.addGenreToTrack(newTrack.getId(), newTrackForm.getGenreList()));
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

	// DB-V5 OK!
	// editTrack
	public TracksModel editTrack(TrackForm trackInfo, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);
		TracksModel track = tracksRepository.findById(trackInfo.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));

		generalFunctionController.checkOwnerShipForRecord(requestedBy.getAccountId(), track.getAccountId());
		if (trackInfo.getTrackName() != "") {
			track.setTrackName(trackInfo.getTrackName());
		}
		if (trackInfo.getTrackDesc() != "") {
			track.setTrackDesc(trackInfo.getTrackDesc());
		}
		tracksRepository.updateBasicTrackInfo(track.getId(), track.getTrackName(), track.getTrackDesc());

		if (trackInfo.getGenreList() != null) {
			track.setGenreTrack(genreController.addGenreToTrack(track.getId(), trackInfo.getGenreList()));
		}

		return track;
	}

	// uploadNewThumbnail
	public void uploadNewThumbnail(int trackId, MultipartFile imageFile, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		TracksModel target = tracksRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), target.getAccountId());

		fileLinkRelController.deleteTargetFileByName(target.getTrackThumbnail());
		String trackThumbnailFileName = fileLinkRelController.insertNewTrackObjectLinkRel(imageFile, 201,
				target.getId());
		tracksRepository.updateTrackThumbnail(target.getId(), trackThumbnailFileName);

	}

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
		} else {
			tracksRepository.updateTrackStatus(trackId, 1001);
			return "The track ID " + trackId + ":" + target.getTrackName() + " is now visible.";
		}

	}

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
}
