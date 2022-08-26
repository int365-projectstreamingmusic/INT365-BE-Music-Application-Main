package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.models.GenreModel;
import com.application.entities.models.GenresTracksModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.AddNewTrackForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.GenreRepository;
import com.application.repositories.GenresTracksRepository;
import com.application.repositories.TracksModelRepository;
import com.application.repositories.UserAccountModelRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.MinioStorageService;

@Service
@PropertySource("generalsetting.properties")
public class TrackManagerController {

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private TracksModelRepository tracksModelRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private GenresTracksRepository genresTracksRepository;

	@Autowired
	private MinioStorageService minioStorageService;

	@Value("${minio.storage.track.music}")
	String minioTrackLocation;

	@Value("${minio.storage.music-thumbnail}")
	String minioTrackThumbnailLocation;

	// AddNewTrack
	public TracksModel addNewTrack(AddNewTrackForm newTrackForm, MultipartFile trackFile, MultipartFile imageFile,
			HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (requestedBy == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ AddNewTrack ] User with this name does not exist.");
		}

		TracksModel newTrack = new TracksModel();

		newTrack.setUserAccountModel(requestedBy);

		newTrack.setDuration((int) trackFile.getSize());
		newTrack.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		newTrack.setViewCount(0);

		newTrack.setAccountId(requestedBy.getAccount_id());

		newTrack.setTrackDesc(newTrackForm.getTrackDesc());
		newTrack.setTrackName(newTrackForm.getTrackName());

		newTrack.setGenreTrack(null);

		String uploadedTrack = minioStorageService.uploadTrackToStorage(trackFile, minioTrackLocation);
		String uploadedImage = minioStorageService.uploadImageToStorage(imageFile, minioTrackThumbnailLocation);

		newTrack.setTrackFile(uploadedTrack);
		newTrack.setThumbnail(uploadedImage);

		newTrack = tracksModelRepository.save(newTrack);

		// Adding genre to the track
		List<GenresTracksModel> addingGenreTrack = new ArrayList<GenresTracksModel>();
		List<GenreModel> targetGenres = newTrackForm.getGenreList();

		if (targetGenres != null && targetGenres.size() > 0) {
			for (int i = 0; i < newTrackForm.getGenreList().size(); i++) {
				addingGenreTrack.add(new GenresTracksModel(
						new GenreTracksCompkey(newTrack.getId(), targetGenres.get(i).getGenre_id()),
						genreRepository.findById(targetGenres.get(i).getGenre_id())
								.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
										HttpStatus.NOT_FOUND, "[ addNewTrack ] Genre not found."))));
			}
			genresTracksRepository.saveAll(addingGenreTrack);
		}
		TracksModel result = tracksModelRepository.findById(newTrack.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addNewTrack ] Track not found."));
		return result;
	}

	// RemoveTrack
	public void removeTrack(int trackId, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		TracksModel targetTrack = tracksModelRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ RemoveTrack ] This track does not exist."));

		if (requestedBy.getAccount_id() != targetTrack.getUserAccountModel().getAccount_id()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ RemoveTrack ] This user is not the owner of this track.");
		}

		tracksModelRepository.deleteById(trackId);

	}

	// EditTrackInfo
	public void editTrackInfo(int trackId, AddNewTrackForm newTrackInfo, HttpServletRequest request) {
		TracksModel targetTrack = tracksModelRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ EditTrackInfo ] This track does not exist."));

		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		if (requestedBy == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ AddNewTrack ] User with this name does not exist.");
		}

	}

	// AddGEnreToTrack
	public void addGenreToTrack(List<GenresTracksModel> genreTrack, int trackId, HttpServletRequest request) {

	}

	// RemoveGreneFromTrack
	public void removeGenreFromTrack(List<GenresTracksModel> genreTrack, int trackId, HttpServletRequest request) {

	}

	// !!!
	// AUTOMATION
	// !!!
}
