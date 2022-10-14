package com.application.controllers;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.ArtistsModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.PlaylistForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.PlaylistRepository;
import com.application.services.GeneralFunctionController;

@Service
@PropertySource("generalsetting.properties")
public class PlaylistController {

	@Autowired
	private PlaylistRepository playlistRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	public static int defaultPlaylistPerPage = 50;
	public static int maxPlaylistPerPage = 250;
	public static int maxTrackPerPlaylist = 100;

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
	//
	// CreateMyPlaylist
	/*public void createMyPlaylist(PlaylistForm newPlaylistForm, HttpServletRequest request) {
		UserAccountModel createdBy = generalFunctionController.getUserAccount(request);

		PlaylistModel newPlaylist = new PlaylistModel();

		newPlaylist.setCreatedDate(new Timestamp().toString());
		newPlaylist.setPlaylistDesc(null);
		newPlaylist.setPlaylistName(null);
		newPlaylist.setPlayTrackStatus(null);
		newPlaylist.setUserAccountModel(createdBy);
		newPlaylist.setThumbnail(null);

	}*/

	// EditMyPlaylist
	public void editMyPlaylist(PlaylistForm newInfoForm, HttpServletRequest request) {

	}

	// UploadeThumbnail
	public void uploadeThumbnail(MultipartFile multipartFile, HttpServletRequest request) {

	}

	// ChangePlaylistStatus
	public void changePlaylistStatus(int PlaylistId, HttpServletRequest request) {

	}

	// DeletePlaylist
	public void deletePlaylist(int PlaylistId, HttpServletRequest request) {

	}

	// ListTrackInPlaylist
	public void listTrackInPlaylist(int playlistId) {

	}

}
