package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

import com.application.entities.copmskeys.PlaylistTrackListCompkey;
import com.application.entities.models.PlayTrackStatusModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.PlaylistTrackListModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.PlaylistForm;
import com.application.entities.submittionforms.PlaylistOutput;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.PlayTrackStatusRepository;
import com.application.repositories.PlaylistRepository;
import com.application.repositories.PlaylistTrackListRepository;
import com.application.repositories.TracksRepository;
import com.application.services.GeneralFunctionController;
import com.application.utilities.ValidatorServices;

@Service
@PropertySource("generalsetting.properties")
@PropertySource("application.properties")
public class PlaylistController {

	@Autowired
	private PlaylistRepository playlistRepository;
	@Autowired
	private PlaylistTrackListRepository playlistTrackListRepository;
	@Autowired
	private PlayTrackStatusRepository playTrackStatusRepository;
	@Autowired
	private TracksRepository tracksRepository;

	@Autowired
	private TrackController trackController;
	@Autowired
	private GeneralFunctionController generalFunctionController;
	@Autowired
	private ValidatorServices validatorServices;
	@Autowired
	private FileLinkRelController fileLinkRelController;
	@Autowired
	private GenreController genreController;
	@Autowired
	private MoodController moodController;

	public static int defaultPlaylistPerPage = 50;
	public static int maxPlaylistPerPage = 250;
	public static int maxTrackPerPlaylist = 100;

	@Value("${application.default.image.playlist}")
	public String defaultPlaylistImage;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// ListAllPlaylist
	public Page<PlaylistModel> listAllPlaylist(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxPlaylistPerPage) {
			pageSize = defaultPlaylistPerPage;
		}
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<PlaylistModel> result;
		result = playlistRepository.listAllPlaylist(searchContent, pageRequest);
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No playlist found.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// ListLatestPlaylist
	public List<PlaylistModel> listLatestPlaylist(int numberOfPlaylist) {
		if (numberOfPlaylist > maxPlaylistPerPage) {
			numberOfPlaylist = maxPlaylistPerPage;
		}
		List<PlaylistModel> result = playlistRepository.listLatestPlaylist(numberOfPlaylist);
		if (result.size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No one mand any new playlist for now.");
		} else {
			return result;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.3 OK!
	// GetPlaylistByID
	public PlaylistOutput getPlaylistById(int id, int page, int pageSize, String searchContent,
			HttpServletRequest request) {

		PlaylistModel playlist = playlistRepository.findById(id).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ]The playlist ID " + id + " does not exist. "));
		PlaylistOutput result = new PlaylistOutput();
		Pageable sendPageRequest = trackController.getPageRequest(page, pageSize);
		result.setPlaylist(playlist);
		result.setTracksInfo(null);
		Page<TracksModel> resultTrack = tracksRepository.listAllByPlaylistId(id, searchContent, sendPageRequest);

		if (request.getHeader(HttpHeaders.AUTHORIZATION) != null) {
			UserAccountModel user = generalFunctionController.getUserAccount(request);
			List<TracksModel> tracks = trackController.getTrackMarking(resultTrack.getContent(), user);
			List<TracksModel> outputTrack = new ArrayList<>();
			for (int i = 0; i < tracks.size(); i++) {
				TracksModel current = tracks.get(i);
				if (tracks.get(i).getPlayTrackStatus().getId() == 1001
						|| user.getAccountId() == tracks.get(i).getOwner().getAccountId()) {
					outputTrack.add(current);
				} else {
					current.setTrackFile(null);
					current.setTrackThumbnail("201-TRACK-NOT-AVAILABLE.png");
					current.setTrackName("NOT AVAILABLE");
					current.setTrackDesc("This track is not available in public.");
					outputTrack.add(current);
				}
			}
			result.setTracksInfo(new PageImpl<>(outputTrack, sendPageRequest, resultTrack.getNumberOfElements()));
		} else {
			result.setTracksInfo(tracksRepository.userListAllByPlaylist(id, searchContent, sendPageRequest));
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// GetMyPlaylist
	public Page<PlaylistModel> getMyPlaylist(int page, int pageSize, String searchContent, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxPlaylistPerPage) {
			pageSize = defaultPlaylistPerPage;
		}
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<PlaylistModel> result;
		result = playlistRepository.listAllPlaylistOwnedBy(owner.getAccountId(), searchContent, pageRequest);
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No playlist found.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// CreateMyPlaylist
	public PlaylistModel createMyPlaylist(PlaylistForm form, MultipartFile image, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		PlaylistModel newPlaylist = new PlaylistModel();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());

		if (!validatorServices.validatePlaylistName(form.getPlaylistName())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_ILLEGAL_NAME, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_ILLEGAL_NAME ] The playlist name must have at least 4 characters and not more than 45.");
		}

		if (form.getPlaylistDesc() == "") {
			form.setPlaylistDesc("A playlist," + form.getPlaylistName() + " created by " + createdBy.getUsername());
		}

		newPlaylist.setPlaylistName(form.getPlaylistName());
		newPlaylist.setPlaylistDesc(form.getPlaylistDesc());

		newPlaylist.setCreatedDate(currentTime.toString());
		// Assign Private Playlist when created.
		newPlaylist.setPlayTrackStatus(playTrackStatusRepository.findById(form.getStatusId()).orElseGet(null));
		newPlaylist.setUserAccountModel(createdBy);
		newPlaylist.setThumbnail(defaultPlaylistImage);

		newPlaylist = playlistRepository.save(newPlaylist);

		if (form.getGenres() != null && !(form.getGenres().size() <= 0)) {
			newPlaylist.setGenres(genreController.addPlaylistGenre(newPlaylist.getId(), form.getGenres()));
		}
		if (form.getMoods() != null && !(form.getMoods().size() <= 0)) {
			newPlaylist.setMoods(moodController.addMoodToPlaylist(newPlaylist.getId(), form.getMoods()));
		}
		if (form.isAutoAddMusic()) {

		}

		if (image != null) {
			String playlistThumbnailFileName = fileLinkRelController.insertNewTrackObjectLinkRel(image, 301,
					newPlaylist.getId());
			newPlaylist.setThumbnail(playlistThumbnailFileName);
			playlistRepository.updatePlaylistThumbnail(newPlaylist.getId(), playlistThumbnailFileName);
		}
		return newPlaylist;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// EditMyPlaylist
	public PlaylistModel editMyPlaylist(PlaylistForm form, MultipartFile image, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		PlaylistModel target = playlistRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The playlist with the ID " + form.getId() + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(createdBy.getAccountId(),
				target.getUserAccountModel().getAccountId());
		if (form.getPlaylistName() != "" && validatorServices.validatePlaylistName(form.getPlaylistName())) {
			target.setPlaylistName(form.getPlaylistName());
		}
		if (form.getPlaylistDesc() != "") {
			target.setPlaylistDesc(form.getPlaylistDesc());
		}
		target = playlistRepository.save(target);

		if (image != null) {
			if (fileLinkRelController.isExistsInRecord(target.getThumbnail())
					&& !target.getThumbnail().equals(defaultPlaylistImage)) {
				fileLinkRelController.deleteTargetFileByName(target.getThumbnail());
			}
			playlistRepository.updatePlaylistThumbnail(form.getId(),
					fileLinkRelController.insertNewTrackObjectLinkRel(image, 301, target.getId()));
		}
		return target;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// UploadeThumbnail
	public void uploadeThumbnail(int playlistId, MultipartFile multipartFile, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		PlaylistModel target = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The playlist with the ID " + playlistId + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(createdBy.getAccountId(),
				target.getUserAccountModel().getAccountId());

		if (fileLinkRelController.isExistsInRecord(target.getThumbnail())
				&& !target.getThumbnail().equals(defaultPlaylistImage)) {
			fileLinkRelController.deleteTargetFileByName(target.getThumbnail());
		}
		String playlistThumbnailName = fileLinkRelController.insertNewTrackObjectLinkRel(multipartFile, 301,
				playlistId);
		playlistRepository.updatePlaylistThumbnail(playlistId, playlistThumbnailName);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// SwitchPlaylistStatus
	public String switchPlaylistStatus(int playlistId, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		PlaylistModel target = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The playlist with the ID " + playlistId + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(createdBy.getAccountId(),
				target.getUserAccountModel().getAccountId());

		if (target.getPlayTrackStatus().getId() == 2002) {
			playlistRepository.updatePlaylistStatus(playlistId, 2001);
			return "The playlist," + target.getPlaylistName() + " is now visible to guest to browse.";
		} else if (target.getPlayTrackStatus().getId() == 2001) {
			playlistRepository.updatePlaylistStatus(playlistId, 2002);
			return "The playlist," + target.getPlaylistName() + " is now hiden from public list.";
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_SAVE_REJECTED, HttpStatus.I_AM_A_TEAPOT,
					"[ USER_SAVE_REJECTED ] This playlise is marked as removed, or in breach of the agreement.");
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ChangePlaylistStatus
	public String changePlaylistStatus(int playlistId, int statusId, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		PlaylistModel target = playlistRepository.findById(playlistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The playlist with the ID " + playlistId + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(createdBy.getAccountId(),
				target.getUserAccountModel().getAccountId());

		PlayTrackStatusModel status = playTrackStatusRepository.findById(statusId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The status with ID " + statusId + " does not exist."));
		playlistRepository.updatePlaylistStatus(playlistId, statusId);
		return "The playlist name " + target.getPlaylistName() + " is now set to '" + status.getStatusDesc() + "'";

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// DeletePlaylist
	public void deletePlaylist(int playlistId, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);
		if (playlistRepository.existByOwner(createdBy.getAccountId(), playlistId) != 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ RECORD_ALREADY_GONE ] No playlist with this ID found in this user' record. It may never exist or not the owner.");
		} else {
			String thumbnailInTarget = playlistRepository.getThunbmailById(playlistId, createdBy.getAccountId());
			if (fileLinkRelController.isExistsInRecord(thumbnailInTarget)) {
				fileLinkRelController.deleteTargetFileByName(thumbnailInTarget);
			}
			playlistRepository.deleteById(playlistId);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Can only add into 1 playlist.
	// AddTrackToPlaylist : With Token required.
	public List<PlaylistTrackListModel> addTrackToPlaylist(PlaylistForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		PlaylistModel playlist = playlistRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] A playlist record with ID " + form.getId() + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(),
				playlist.getUserAccountModel().getAccountId());
		// ----
		List<PlaylistTrackListModel> trackList = playlistTrackListRepository.listTrack(form.getId());

		for (int i = 0; i < form.getTrackIdList().size(); i++) {
			PlaylistTrackListModel newTrack = new PlaylistTrackListModel();
			newTrack.setId(new PlaylistTrackListCompkey(form.getTrackIdList().get(i).getTrackId(), form.getId()));
			newTrack.setIsSkipped(0);
			newTrack.setPlaceInList(trackList.size() + i + 1);

			boolean isRepeated = false;
			for (int j = 0; j < trackList.size(); j++) {
				if (newTrack.getId().getTrack_id() == trackList.get(j).getId().getTrack_id()) {
					isRepeated = true;
					break;
				}
			}
			if (!isRepeated) {
				trackList.add(newTrack);
				// If track with that ID does not exist, will do nothing.
				try {
					playlistTrackListRepository.save(newTrack);
				} catch (Exception exc) {

				}
			}
		}
		return trackList;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Can only remove from 1 playlist.
	// RemoveTrackToPlaylist : With Token required.
	public List<PlaylistTrackListModel> removeTrackFromPlaylist(PlaylistForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		PlaylistModel playlist = playlistRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] A playlist record with ID " + form.getId() + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(),
				playlist.getUserAccountModel().getAccountId());
		// ----
		List<PlaylistTrackListModel> trackList = playlistTrackListRepository.listTrack(form.getId());

		for (int i = 0; i < form.getTrackIdList().size(); i++) {
			PlaylistTrackListModel currentTrack = null;
			PlaylistTrackListCompkey id = new PlaylistTrackListCompkey(form.getTrackIdList().get(i).getTrackId(),
					form.getId());

			boolean isGoneFromTheList = true;
			for (int j = 0; j < trackList.size(); j++) {
				if (id.equals(trackList.get(j).getId())) {
					currentTrack = trackList.get(j);
					isGoneFromTheList = false;
					break;
				}
			}
			if (!isGoneFromTheList) {
				trackList.remove(currentTrack);
				playlistTrackListRepository.deleteById(id);
			}

		}
		return trackList;
	}

}
