package com.application.apis;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.ArtistController;
import com.application.controllers.PlayHistoryController;
import com.application.controllers.TrackCountController;
import com.application.controllers.TrackManagerController;
import com.application.controllers.TrackMarkingController;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.ArtistsTrackModel;
import com.application.entities.models.PlayHistoryModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.ArtistTrackForm;
import com.application.entities.submittionforms.ArtistsEditForm;
import com.application.repositories.PlayHistoryRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("test/")
public class TestingApis {

	@Autowired
	private ValidatorServices validatorServices;
	@Autowired
	private TrackMarkingController trackMarkingController;
	@Autowired
	private TrackCountController trackCountController;
	@Autowired
	private ArtistController artistController;

	// ARTIST MANAGER

	@GetMapping("ListArtists")
	public ResponseEntity<Page<ArtistsModel>> listArtists(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(artistController.listAllArtists(page, pageSize, searchContent));
	}

	@GetMapping("MyArtists")
	public ResponseEntity<Page<ArtistsModel>> getMyArtists(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "100") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(artistController.listMyArtistList(page, pageSize, searchContent, request));
	}

	@DeleteMapping("DeleteArtist")
	public ResponseEntity<HttpStatus> deleteArtist(@RequestParam(required = true) int artistId,
			HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/artist/addMyArtistToTrack").toString());
		artistController.deleteArtist(artistId, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	@GetMapping("IsAnyInThisTrack")
	public ResponseEntity<Boolean> isAnyInThisTrack(@RequestParam(required = true) int trackId) {
		return ResponseEntity.ok().body(artistController.checkIfArtistsExistInTrack(trackId));
	}

	@GetMapping("ListArtistInTrack")
	public ResponseEntity<Page<ArtistsModel>> listArtistInTrack(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(required = true) int trackId) {
		return ResponseEntity.ok().body(artistController.listArtistInCurrentTrack(page, pageSize, trackId));
	}

	@PutMapping("EditMyArtist")
	public ResponseEntity<ArtistsModel> editMyArtist(@RequestBody ArtistsEditForm newInformation,
			HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/artist/editMyArtist").toString());
		return ResponseEntity.created(uri).body(artistController.editArtistInfo(newInformation, request));
	}

	@PutMapping("EditMyArtistInTrack")
	public ResponseEntity<ArtistsTrackModel> editMyArtistInTrack(
			@RequestBody(required = true) ArtistTrackForm newInformation, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/artist/editMyArtistInTrack").toString());
		return ResponseEntity.created(uri).body(artistController.editArtistTrackDescription(newInformation, request));
	}

	@PostMapping("AddMyArtistToTrack")
	public ResponseEntity<ArtistsTrackModel> addMyArtistToTrack(
			@RequestBody(required = true) ArtistTrackForm newArtistInTrack, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/artist/addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(artistController.addArtistToTrack(newArtistInTrack, request));
	}

	@DeleteMapping("DeleteMyArtistFromTrack")
	public ResponseEntity<HttpStatus> deleteMyArtistFromTrack(@RequestBody(required = true) ArtistTrackForm targetForm,
			HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/artist/addMyArtistToTrack").toString());
		artistController.DeleteArtistFromTrack(targetForm, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// VIEW COUNT

	@GetMapping("AddCustomTrackCount")
	public ResponseEntity<Map<String, Object>> addCustomTrackCount(@RequestParam int trackId, @RequestParam int nWeek) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/trackcount/addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(trackCountController.addCustomViewCount(trackId, nWeek));
	}

}
