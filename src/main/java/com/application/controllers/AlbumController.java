package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.AlbumModel;
import com.application.entities.models.PlayTrackStatusModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.AlbumForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.AlbumRepository;
import com.application.repositories.PlayTrackStatusRepository;
import com.application.services.GeneralFunctionController;

@Service
public class AlbumController {

	@Autowired
	private AlbumRepository albumRepository;
	@Autowired
	private PlayTrackStatusRepository playTrackStatusRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	public static int defaultAlbumPerPage = 50;
	public static int maxAlbumPerPage = 250;
	public static int maxAlbumlist = 100;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// List all album
	public Page<AlbumModel> listAlbum(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxAlbumPerPage) {
			pageSize = defaultAlbumPerPage;
		}

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<AlbumModel> result;
		result = albumRepository.listAll(searchContent, pageRequest);
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No album found.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Only list my album
	public Page<AlbumModel> myAlbum(int page, int pageSize, String searchContent, HttpServletRequest servletRequest) {
		UserAccountModel owner = generalFunctionController.getUserAccount(servletRequest);

		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxAlbumPerPage) {
			pageSize = defaultAlbumPerPage;
		}

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<AlbumModel> result;
		result = albumRepository.listAll(searchContent, owner.getAccountId(), pageRequest);
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No album found.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Create album : Object
	public AlbumModel createAlbum(AlbumForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		if (form.getStatusId() <= 3000 || form.getStatusId() >= 4000) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_INVALID_STATUS ] " + form.getStatusId() + " is not a valid status ID.");
		}
		PlayTrackStatusModel status = playTrackStatusRepository.findById(form.getStatusId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Status does not found."));
		if (form.getDesc() == "") {
			form.setDesc(form.getName() + " is created by " + owner.getProfileName());
		}

		AlbumModel newAlbum = new AlbumModel();
		newAlbum.setAlbumName(form.getName());
		newAlbum.setAlbumDescription(form.getDesc());
		newAlbum.setOwner(owner);
		newAlbum.setStatus(status);
		newAlbum = albumRepository.save(newAlbum);

		return newAlbum;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// Edit album
	public AlbumModel editAlbum(AlbumForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		AlbumModel album = albumRepository.findById(form.getId()).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The album with id " + form.getId() + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), album.getOwner().getAccountId());

		if (form.getDesc() != "") {
			album.setAlbumDescription(form.getDesc());
		}
		album.setAlbumName(form.getName());
		if (form.getStatusId() != 0) {
			if (form.getStatusId() <= 3000 || form.getStatusId() >= 4000) {
				throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
						"[ RECORD_INVALID_STATUS ] Status ID" + form.getStatusId() + " does not valid");
			}
			PlayTrackStatusModel status = playTrackStatusRepository.findById(form.getStatusId())
					.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
							HttpStatus.NOT_FOUND,
							"[ BROWSE_NO_RECORD_EXISTS ] Status ID " + form.getStatusId() + " does not exist."));
			album.setStatus(status);
		}
		album = albumRepository.save(album);
		return album;
	}

	// Delete album
	public void deleteAlbum(int albumId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		AlbumModel album = albumRepository.findById(albumId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.RECORE_ALREADY_EXIST, HttpStatus.NOT_FOUND,
						"[ RECORE_ALREADY_EXIST ] The album with id " + albumId + " does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), album.getOwner().getAccountId());
		albumRepository.deleteById(albumId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// NO API
	// Create album : Fast , Just give it a name when adding a new track.
	public AlbumModel createAlbum(String albumName, UserAccountModel userAccount) {
		AlbumModel newAlbum = new AlbumModel();
		newAlbum.setAlbumName(albumName);
		newAlbum.setAlbumDescription(albumName + " album is created by " + userAccount.getProfileName());
		newAlbum.setStatus(playTrackStatusRepository.findById(3002).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
				"[ BROWSE_NO_RECORD_EXISTS ] This is because the status is not present in the database. Check that real quick!")));
		newAlbum.setOwner(userAccount);
		albumRepository.save(newAlbum);
		return newAlbum;
	}
}
