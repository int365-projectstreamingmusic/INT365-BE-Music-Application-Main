package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.models.GenreModel;
import com.application.entities.models.GenresTracksModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.TrackForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.GenreRepository;
import com.application.repositories.PlayTrackRepository;
import com.application.repositories.TracksRepository;
import com.application.repositories.UserAccountRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.MinioStorageService;

@Service
public class TrackController {

	@Value("${general.track.default-page-size}")
	private int trackDefaultSize;
	@Value("${general.track.max-page-size}")
	private int trackMaxPageSize;

	@Autowired
	private UserAccountRepository userAccountModelRepository;
	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private GenreRepository genreRepository;
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

	// WIP
	// AddNewTrack
	public TracksModel addNewTrack(TrackForm newTrackForm, MultipartFile trackFile, MultipartFile imageFile,
			HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		TracksModel newTrack = new TracksModel();
		newTrack.setDuration((int) trackFile.getSize());
		newTrack.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		newTrack.setAccountId(requestedBy.getAccountId());

		newTrack.setTrackDesc(newTrackForm.getTrackDesc());
		newTrack.setTrackName(newTrackForm.getTrackName());

		newTrack.setGenreTrack(null);
		newTrack.setTrackFile("-");

		newTrack.setViewCount(0);
		newTrack.setFavoriteCount(0);
		newTrack.setPlayTrackStatus(playTrackRepository.findById(1001).get());

		newTrack = tracksRepository.save(newTrack);

		// Adding genre to the track
		List<GenresTracksModel> addingGenreTrack = new ArrayList<GenresTracksModel>();
		List<GenreModel> targetGenres = newTrackForm.getGenreList();

		if (targetGenres != null && targetGenres.size() > 0) {
			for (int i = 0; i < newTrackForm.getGenreList().size(); i++) {
				addingGenreTrack.add(new GenresTracksModel(
						new GenreTracksCompkey(newTrack.getId(), targetGenres.get(i).getGenreId()),
						genreRepository.findById(targetGenres.get(i).getGenreId())
								.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
										HttpStatus.NOT_FOUND, "[ addNewTrack ] Genre not found."))));
			}
		}
		TracksModel result = tracksRepository.findById(newTrack.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addNewTrack ] Track not found."));

		// Save image and track file.
		if (imageFile != null) {
			String trackThumbnailFIleName = fileLinkRelController.insertNewTrackObjectLinkRel(imageFile, 201,
					result.getId());
			newTrack.setTrackThumbnail(trackThumbnailFIleName);
		}
		String uploadedTrack = minioStorageService.uploadTrackToStorage(trackFile, minioTrackLocation);
		result.setTrackFile(uploadedTrack);
		tracksRepository.updateTrackFileName(uploadedTrack, result.getId());
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
}
