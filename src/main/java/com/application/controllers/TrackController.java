package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.AlbumModel;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.AlbumOutput;
import com.application.entities.submittionforms.ArtistForm;
import com.application.entities.submittionforms.PlaylistOutputTrack;
import com.application.entities.submittionforms.TrackForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.AlbumRepository;
import com.application.repositories.PlayTrackStatusRepository;
import com.application.repositories.TracksRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.MinioStorageService;

@Service
@PropertySource("application.properties")
public class TrackController {

	@Value("${general.track.default-page-size}")
	private int trackDefaultSize;
	@Value("${general.track.max-page-size}")
	private int trackMaxPageSize;
	@Value("${application.default.image.track}")
	private String defaultTrackImage;

	@Autowired
	private TracksRepository tracksRepository;
	@Autowired
	private PlayTrackStatusRepository playTrackStatusRepository;
	@Autowired
	private AlbumRepository albumRepository;

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
	@Autowired
	private ArtistController artistController;

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
		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			UserAccountModel user = generalFunctionController.getUserAccount(request);
			return new PageImpl<>(getTrackMarking(result.stream().collect(Collectors.toList()), user), sendPageRequest,
					result.getTotalElements());
		} else {
			return result;
		}
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
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_FORBIDDEN, HttpStatus.FORBIDDEN,
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
		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			UserAccountModel user = generalFunctionController.getUserAccount(request);
			return getTrackMarking(result, user);
		} else {
			return result;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// getTopTrack
	public List<TracksModel> getTopTrackOfNDay(int numberOfTracks, HttpServletRequest request) {
		if (numberOfTracks > trackMaxPageSize) {
			numberOfTracks = trackMaxPageSize;
		}
		List<TracksModel> result = tracksRepository.listTopTrack(numberOfTracks);
		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			return getTrackMarking(result, generalFunctionController.getUserAccount(request));
		} else {
			return result;
		}
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

		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			return getTrackMarking(result, generalFunctionController.getUserAccount(request));
		} else {
			return result;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get tracks in albums.
	public AlbumOutput listTrackByAlbum(int albumId, int page, int pageSize, String searchContent,
			HttpServletRequest request) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<TracksModel> trackResult;
		AlbumOutput result = new AlbumOutput();
		result.setAlbum(albumRepository.findById(albumId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The album with this ID does not exist.")));

		trackResult = tracksRepository.listAllByAlbum(albumId, searchContent, sendPageRequest);

		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			result.setTracks(new PageImpl<>(
					getTrackMarking(trackResult.stream().collect(Collectors.toList()),
							generalFunctionController.getUserAccount(request)),
					sendPageRequest, trackResult.getTotalElements()));
		} else {
			result.setTracks(trackResult);
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Get list of track in playlist
	public Page<TracksModel> listTrackByPlaylist(int playlistId, int page, int pageSize, String searchContent,
			boolean isByPlaylistOwner, HttpServletRequest request) {
		Pageable sendPageRequest = getPageRequest(page, pageSize);
		Page<TracksModel> result;
		if (isByPlaylistOwner) {
			result = tracksRepository.listAllByPlaylistId(playlistId, searchContent, sendPageRequest);
		} else {
			result = tracksRepository.userListAllByPlaylist(playlistId, searchContent, sendPageRequest);
		}
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ TrackController ] Found nothing here. Seems like there is no track here.");
		}
		return new PageImpl<>(getTrackMarking(result.stream().collect(Collectors.toList()),
				generalFunctionController.getUserAccount(request)), sendPageRequest, result.getTotalElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// List tracks in the playground that is owned by that user.
	public Page<PlaylistOutputTrack> getTrackInPlaylistById(PlaylistModel playlist, String searcnContent,
			Pageable pageRequest, UserAccountModel user) {
		Page<TracksModel> trackResult = tracksRepository.listAllByPlaylistId(playlist.getId(), searcnContent,
				pageRequest);
		if (trackResult.getContent().size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This playlist has no music for a moment.");
		} else {
			List<TracksModel> trackList = getTrackMarking(trackResult.getContent(), user);
			List<PlaylistOutputTrack> outputTrack = new ArrayList<>();
			for (int i = 0; i < trackList.size(); i++) {
				PlaylistOutputTrack current = new PlaylistOutputTrack();
				// If not public, set null.
				if (trackList.get(i).getPlayTrackStatus().getId() != 1001
						&& trackList.get(i).getAccountId() != user.getAccountId()
						&& user.getAccountId() == playlist.getUserAccountModel().getAccountId()) {
					current.setFavorite(trackList.get(i).isFavorite());
					current.setId(trackList.get(i).getId());
					current.setPlayground(trackList.get(i).isPlayground());
					current.setTrackFile(null);
					current.setTrackName("Unavailable");
					current.setTrackThumbnail(defaultTrackImage);
					current.setStatus(trackList.get(i).getPlayTrackStatus());
					outputTrack.add(current);
				} else {
					current.setFavorite(trackList.get(i).isFavorite());
					current.setId(trackList.get(i).getId());
					current.setPlayground(trackList.get(i).isPlayground());
					current.setTrackFile(trackList.get(i).getTrackFile());
					current.setTrackName(trackList.get(i).getTrackName());
					current.setTrackThumbnail(trackList.get(i).getTrackThumbnail());
					current.setStatus(trackList.get(i).getPlayTrackStatus());
					outputTrack.add(current);
				}
			}
			return new PageImpl<>(outputTrack, pageRequest, trackResult.getNumberOfElements());
		}
	}

	public Page<PlaylistOutputTrack> getTrackInPlaylistById(int playlistId, String searcnContent,
			Pageable pageRequest) {
		Page<TracksModel> trackResult = tracksRepository.listAllByPlaylistId(playlistId, searcnContent, pageRequest);
		if (trackResult.getContent().size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This playlist has no music for a moment.");
		} else {
			List<TracksModel> trackList = trackResult.getContent();
			List<PlaylistOutputTrack> outputTrack = new ArrayList<>();
			for (int i = 0; i < trackList.size(); i++) {
				PlaylistOutputTrack current = new PlaylistOutputTrack();
				// If not public, set null.
				if (!(trackList.get(i).getPlayTrackStatus().getId() != 1001)) {
					current.setId(trackList.get(i).getId());
					current.setTrackFile(trackList.get(i).getTrackFile());
					current.setTrackName(trackList.get(i).getTrackName());
					current.setTrackThumbnail(trackList.get(i).getTrackThumbnail());
					current.setStatus(trackList.get(i).getPlayTrackStatus());
					outputTrack.add(current);
				}
			}
			return new PageImpl<>(outputTrack, pageRequest, trackResult.getNumberOfElements());
		}

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Send page request back to another function.
	public Pageable getPageRequest(int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > trackMaxPageSize) {
			pageSize = trackDefaultSize;
		}

		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		return sendPageRequest;
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
	//
	// Management zone for creator only.
	//
	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// AddNewTrack
	public TracksModel addNewTrack(TrackForm form, MultipartFile trackFile, MultipartFile imageFile,
			HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);

		TracksModel newTrack = new TracksModel();
		// General information.
		newTrack.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		newTrack.setAccountId(requestedBy.getAccountId());

		// Information given in the form.
		newTrack.setTrackDesc(form.getTrackDesc());
		newTrack.setTrackName(form.getTrackName());

		// No view or favorite at the begining.
		newTrack.setViewCount(0);
		newTrack.setFavoriteCount(0);
		newTrack.setDuration(0);

		// Track status is hiden when first uploaded.
		newTrack.setPlayTrackStatus(playTrackStatusRepository.findById(form.getStatusId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID, HttpStatus.I_AM_A_TEAPOT,
						"[ SAVE_FILE_INVALID ] Invalid track status ID.")));

		// Thinks to add later.
		newTrack.setGenreTrack(null);
		newTrack.setMoods(null);
		newTrack.setTrackFile("-");
		newTrack.setAlbums(null);
		newTrack.setArtistTracks(null);
		newTrack = tracksRepository.save(newTrack);

		// Adding genre to the track
		if (form.getGenreList() != null && !(form.getGenreList().size() <= 0)) {
			newTrack.setGenreTrack(genreController.addGenreToTrack(newTrack.getId(), form.getGenreList()));
		}
		if (form.getMoodList() != null && !(form.getMoodList().size() <= 0)) {
			newTrack.setMoods(moodController.addMoodToTrack(newTrack.getId(), form.getMoodList()));
		}

		// Saving artist
		if (form.getArtist() != null || form.getArtist() != "") {
			ArtistForm artistForm = new ArtistForm();
			artistForm.setArtistName(form.getArtist());
			artistForm.setArtistBio("An artist " + form.getArtist() + " is added by " + requestedBy.getProfileIamge());
			ArtistsModel artist = artistController.newArtist(artistForm, requestedBy);
			artistController.newArtistTrack(newTrack.getId(), artist);
			newTrack.setArtistTracks(artistController.listArtistTrack(newTrack.getId()));
		}

		// Saving new album
		if (form.getAlbumName() != null && form.getAlbumName() != "") {
			if (albumRepository.existsByAlbumName(form.getAlbumName())) {
				newTrack.setAlbums(albumRepository.findByAlbumName(form.getAlbumName()));
				tracksRepository.updateTrackAlbum(newTrack.getId(), newTrack.getAlbums().getId());
			} else {
				AlbumModel newAlbum = new AlbumModel();
				newAlbum.setAlbumName(form.getAlbumName());
				newAlbum.setAlbumDescription(
						"The album " + form.getAlbumName() + " is created by " + requestedBy.getUsername());
				newAlbum.setStatus(playTrackStatusRepository.findById(3001).orElse(null));
				newAlbum.setOwner(requestedBy);
				albumRepository.save(newAlbum);
				newTrack.setAlbums(newAlbum);
			}
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
	public TracksModel editTrack(TrackForm form, MultipartFile image, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.getUserAccount(request);
		TracksModel target = tracksRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Track with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(requestedBy.getAccountId(), target.getAccountId());

		if (form.getAlbumName() != "" && albumRepository.existsById(target.getAlbums().getId())) {
			albumRepository.updateNewAlbumName(target.getAlbums().getId(), form.getAlbumName());
		}

		if (form.getTrackName() != "") {
			target.setTrackName(form.getTrackName());
		}
		if (form.getTrackDesc() != "") {
			target.setTrackDesc(form.getTrackDesc());
		}

		tracksRepository.save(target);
		// tracksRepository.updateBasicTrackInfo(target.getId(), target.getTrackName(),
		// target.getTrackDesc());

		if (form.getGenreList() != null) {
			target.setGenreTrack(genreController.addGenreToTrack(target.getId(), form.getGenreList()));
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

		return tracksRepository.findById(target.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR,
						"[ CORE_INTERNAL_SERVER_ERROR ] Unknown reason at EDITTRACK function."));
	}

	public void updateAlbumName(int id, String newName) {
		albumRepository.updateNewAlbumName(id, newName);
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
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SAVE_REJECTED, HttpStatus.FORBIDDEN,
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
		tracksRepository.deleteFromDatabase(trackId);
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
	//
	// Automation - No API
	//
	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// Check if the track is favorite.
	public List<TracksModel> getTrackMarking(List<TracksModel> incomingList, UserAccountModel userAccount) {
		try {
			List<TracksModel> result = new ArrayList<>();
			for (int i = 0; i < incomingList.size(); i++) {
				TracksModel currentTrackCheck = incomingList.get(i);
				if (trackMarkingController.checkIfFavorite(userAccount.getAccountId(), incomingList.get(i).getId())) {
					currentTrackCheck.setFavorite(true);
				}
				if (trackMarkingController.checkIfPlayground(userAccount.getAccountId(), incomingList.get(i).getId())) {
					currentTrackCheck.setPlayground(true);
				}
				result.add(currentTrackCheck);
			}
			return incomingList;
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ CORE_INTERNAL_SERVER_ERROR ] " + e.getLocalizedMessage());
		}
	}
}
