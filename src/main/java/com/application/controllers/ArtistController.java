package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.entities.copmskeys.ArtistTrackCompKey;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.ArtistsTrackModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ArtistTrackForm;
import com.application.entities.submittionforms.ArtistsEditForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ArtistsRepository;
import com.application.repositories.ArtistsTracksRepository;
import com.application.utilities.GeneralFunctionController;

@RestController
@RequestMapping("api/manager/artist/")
public class ArtistController {

	public static int defaultArtistsPerPage = 50;
	public static int maxArtistsPerPage = 250;

	@Autowired
	private ArtistsRepository artistRepository;
	@Autowired
	private ArtistsTracksRepository artistTracksRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// ListAllArtists
	public Page<ArtistsModel> listAllArtists(int page, int pageSize, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxArtistsPerPage) {
			pageSize = defaultArtistsPerPage;
		}
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<ArtistsModel> result;

		if (searchContent == "") {
			result = artistRepository.findAll(pageRequest);
		} else {
			result = artistRepository.listArtistByArtistName(searchContent, pageRequest);
		}
		return result;
	}

	// OK!
	// ListMyArtistList
	public Page<ArtistsModel> listMyArtistList(int page, int pageSize, String searchContent,
			HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.checkUserAccountExist(request);
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxArtistsPerPage) {
			pageSize = defaultArtistsPerPage;
		}
		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<ArtistsModel> result;

		if (searchContent == "") {
			result = artistRepository.listArtistByUserId(requestedBy.getAccountId(), pageRequest);
		} else {
			result = artistRepository.listArtistByUserIdAndArtistName(requestedBy.getAccountId(), searchContent,
					pageRequest);
		}
		return result;
	}

	// OK!
	// DeleteArtist
	public void deleteArtist(int artistId, HttpServletRequest request) {
		UserAccountModel requestedBy = generalFunctionController.checkUserAccountExist(request);

		ArtistsModel targetArtist = artistRepository.findById(artistId).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
				"[ BROWSE_NO_RECORD_EXISTS ] The artist with ID " + artistId + " does not exist in the database."));

		if (requestedBy.getAccountId() != targetArtist.getUserAccount().getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.FORBIDDEN,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not an owner of this record.");
		} else {
			artistRepository.deleteByArtistId(artistId);
		}

	}

	// OK!
	// CheckIfArtistsExistInTrack
	public boolean checkIfArtistsExistInTrack(int trackId) {
		if (artistRepository.isExistInTrackId(trackId) == 1) {
			return true;
		} else {
			return false;
		}
	}

	// OK!
	// ListArtistInCurrentTrack
	public Page<ArtistsModel> listArtistInCurrentTrack(int page, int pageSize, int trackId) {

		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > maxArtistsPerPage) {
			pageSize = defaultArtistsPerPage;
		}

		Pageable pageRequest = PageRequest.of(page, pageSize);
		Page<ArtistsModel> result;

		result = artistRepository.listArtistInCurrentTrack(trackId, pageRequest);
		System.out.println("" + result.getNumberOfElements());
		if (result.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] No record for this track. This track has no artist.");
		}

		return result;
	}

	// OK!
	// EditArtistInfo
	public ArtistsModel editArtistInfo(ArtistsEditForm newInformation, HttpServletRequest request) {

		UserAccountModel owner = generalFunctionController.checkUserAccountExist(request);

		ArtistsModel targetArtistRecord = artistRepository.findById(newInformation.getArtistId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] This artist does not exist."));

		if (targetArtistRecord.getUserAccount().getAccountId() != owner.getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.FORBIDDEN,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not an owner of this record.");
		}

		if (newInformation.getArtistBio() != null && newInformation.getArtistBio() != "") {
			targetArtistRecord.setArtistBio(newInformation.getArtistBio());
		}

		if (newInformation.getArtistName() != null && newInformation.getArtistName() != "") {
			targetArtistRecord.setArtistName(newInformation.getArtistName());
		}

		artistRepository.updateArtistDetail(targetArtistRecord.getArtistId(), targetArtistRecord.getArtistName(),
				targetArtistRecord.getArtistBio());

		return targetArtistRecord;
	}

	// OK!
	// EditArtistTrackDescription
	public ArtistsTrackModel editArtistTrackDescription(ArtistTrackForm newInfo, HttpServletRequest request) {

		CheckIncomingArtistTrackForm(newInfo);
		UserAccountModel owner = generalFunctionController.checkUserAccountExist(request);

		ArtistsTrackModel targetArtistTrackRecord = artistTracksRepository
				.findById(new ArtistTrackCompKey(newInfo.getTrackId(), newInfo.getArtistId()))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] This artist does not exist in this track."));
		ArtistsModel targetArtistRecord = artistRepository.findById(newInfo.getArtistId()).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] This artist does not exist in the database."));

		if (targetArtistRecord.getUserAccount().getAccountId() != owner.getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.FORBIDDEN,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not an owner of this record.");
		}

		targetArtistTrackRecord.setArtistDescription(newInfo.getDescription());
		artistTracksRepository.updateArtistTrackDescription(newInfo.getTrackId(), newInfo.getArtistId(),
				newInfo.getDescription());
		return targetArtistTrackRecord;
	}

	// OK!
	// DeleteArtistFromTrack
	public void DeleteArtistFromTrack(ArtistTrackForm targetForm, HttpServletRequest request) {
		CheckIncomingArtistTrackForm(targetForm);
		UserAccountModel owner = generalFunctionController.checkUserAccountExist(request);

		ArtistsTrackModel targetArtistInTrack = artistTracksRepository
				.findById(new ArtistTrackCompKey(targetForm.getTrackId(), targetForm.getArtistId())).orElseThrow(
						() -> new ExceptionFoundation(EXCEPTION_CODES.RECORD_ALREADY_GONE, HttpStatus.I_AM_A_TEAPOT,
								"[ RECORD_ALREADY_GONE ] This record does not exist, nothing to delete."));

		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(),
				targetArtistInTrack.getArtistsModel().getUserAccount().getAccountId());
		artistTracksRepository.deleteById(targetArtistInTrack.getArtistTrackID());
	}

	// OK!
	// AddArtistToTrack
	public ArtistsTrackModel addArtistToTrack(ArtistTrackForm newForm, HttpServletRequest request) {

		CheckIncomingArtistTrackForm(newForm);

		UserAccountModel owner = generalFunctionController.checkUserAccountExist(request);

		ArtistsModel targetArtist = artistRepository.findById(newForm.getArtistId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] This artist does not exist in this track."));

		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(),
				targetArtist.getUserAccount().getAccountId());

		ArtistsTrackModel newArtistTrack = new ArtistsTrackModel();
		newArtistTrack.setArtistTrackID(new ArtistTrackCompKey(newForm.getTrackId(), newForm.getArtistId()));
		newArtistTrack.setArtistDescription(newForm.getDescription());

		if (artistTracksRepository.existsById(newArtistTrack.getArtistTrackID())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORE_ALREADY_EXIST, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORE_ALREADY_EXIST ] This artist is already present in the track.");
		}

		newArtistTrack = artistTracksRepository.save(newArtistTrack);
		newArtistTrack.setArtistsModel(targetArtist);

		return newArtistTrack;
	}

	// CheckIncomingArtistTrackForm
	private void CheckIncomingArtistTrackForm(ArtistTrackForm form) {
		if (form.getArtistId() <= 0 || form.getTrackId() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] TrackId " + form.getArtistId() + " and artistId " + form.getTrackId()
							+ " never exist. Recheck your variable again because we got 0 or less.");
		}
	}

}
