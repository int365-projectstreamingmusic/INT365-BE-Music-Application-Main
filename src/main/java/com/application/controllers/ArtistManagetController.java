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
import com.application.entities.submittionforms.ArtistForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ArtistsRepository;
import com.application.repositories.ArtistsTracksRepository;
import com.application.repositories.UserAccountModelRepository;
import com.application.utilities.JwtTokenUtills;

@RestController
@RequestMapping("api/manager/artist/")
public class ArtistManagetController {

	public static int defaultArtistsPerPage = 50;
	public static int maxArtistsPerPage = 250;

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private ArtistsRepository artistRepository;
	@Autowired
	private ArtistsTracksRepository artistTracksRepository;

	// OK!
	// GetArtistsAddedByMe
	public Page<ArtistsModel> getArtistsAddedMyMe(int page, int size, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		if (page < 0) {
			page = 0;
		}
		if (size < 1 || size > maxArtistsPerPage) {
			size = defaultArtistsPerPage;
		}
		Pageable pageRequest = PageRequest.of(page, size);
		return artistRepository.getArtistByUserAccountModel(requestedBy.getAccountId(), pageRequest);
	}

	// AddNewArtist
	public ArtistsModel addNewArtist(ArtistForm newArtist, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		ArtistsModel addingArtist = new ArtistsModel();
		addingArtist.setArtistBio(newArtist.getArtistBio());
		addingArtist.setArtistName(newArtist.getArtistName());
		addingArtist.setUserAccount(requestedBy);

		return artistRepository.save(addingArtist);
	}

	// RemoveArtist
	public void removeArtist(int artistId, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		ArtistsModel targetArtist = artistRepository.findById(artistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ removeArtist ] Artist with this ID does not exist."));

		if (requestedBy.getAccountId() != targetArtist.getArtistId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ removeArtist ] This user is not the owner of this record.");
		}

		artistRepository.deleteById(artistId);
	}

	// AddArtistsToTrack
	public ArtistsTrackModel addArtistToTrack(int artistId, int trackId, String description,
			HttpServletRequest request) {
		ArtistsTrackModel newArtistTrack = new ArtistsTrackModel();
		newArtistTrack.setArtistTrackID(new ArtistTrackCompKey(trackId, artistId));
		newArtistTrack.setArtistDescription(description);
		return artistTracksRepository.save(newArtistTrack);
	}

	// RemoveArtistsFromTrack
	public void removeArtistsFromTrack(int artistId, int trackId, HttpServletRequest request) {
		ArtistsTrackModel targetTrack = artistTracksRepository.findById(new ArtistTrackCompKey(trackId, artistId))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ removeArtistsFromTrack ] A record with this ID does not exist."));

		artistTracksRepository.delete(targetTrack);
	}

	// UpdateArtistName
	public void updateArtistName(int artistId, String newName, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		ArtistsModel targetArtist = artistRepository.findById(artistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ updateArtistName ] Artists with this ID is not exist."));

		if (requestedBy.getAccountId() != targetArtist.getUserAccount().getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ updateArtistName ] You are not the record owner of this artist.");
		}

		artistRepository.updateArtistName(artistId, newName);
	}

	// UpdateArtistBio
	public void updateArtistBio(int artistId, String newBio, HttpServletRequest request) {
		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		ArtistsModel targetArtist = artistRepository.findById(artistId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ updateArtistBio ] Artists with this ID is not exist."));

		if (requestedBy.getAccountId() != targetArtist.getUserAccount().getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ updateArtistBio ] You are not the record owner of this artist.");
		}

		artistRepository.updateArtistBio(artistId, newBio);
	}
}
