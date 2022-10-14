package com.application.apis;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Track;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.ArtistController;
import com.application.controllers.TrackController;
import com.application.controllers.TrackCountController;
import com.application.controllers.TrackGeneralController;
import com.application.controllers.TrackMarkingController;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.ArtistsTrackModel;
import com.application.entities.models.TracksModel;
import com.application.entities.submittionforms.ArtistTrackForm;
import com.application.entities.submittionforms.ArtistsEditForm;
import com.application.entities.submittionforms.TrackForm;
import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("/api/artist/")
public class A3CreatorApis {
	@Autowired
	private TrackGeneralController trackGeneralController;
	@Autowired
	private ValidatorServices validatorServices;
	@Autowired
	private TrackMarkingController trackMarkingController;
	@Autowired
	private TrackCountController trackCountController;
	@Autowired
	private ArtistController artistController;
	@Autowired
	private TrackController trackController;

	private static String mapping = "/api/artist/";

	// --------------------
	// Track
	// --------------------

	// DB-V5 OK!
	@GetMapping("tracks")
	public ResponseEntity<Page<TracksModel>> getMyTrack(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackGeneralController.listMyTrack(page, pageSize, searchContent, request));
	}

	//
	@PostMapping("newTrack")
	public ResponseEntity<TracksModel> createNewTrack(@RequestPart TrackForm newTrack,
			@RequestPart MultipartFile trackFile, @RequestPart MultipartFile imageFile, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "newTrack").toString());
		return ResponseEntity.created(uri).body(trackController.addNewTrack(newTrack, trackFile, imageFile, request));
	}

	// DB-V5 OK!
	@PutMapping("edit-track")
	public ResponseEntity<TracksModel> editTrack(@RequestBody TrackForm track, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "edit-track").toString());
		return ResponseEntity.created(uri).body(trackController.editTrack(track, request));
	}

	//
	@PutMapping("trackstatus")
	public ResponseEntity<String> changeStatus(@RequestParam(required = true) int trackId, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "trackstatus").toString());
		return ResponseEntity.created(uri).body(null);
	}

	//
	// DANGEROUS METHOD
	@DeleteMapping("deletetrack")
	public ResponseEntity<String> deleteMyTrack(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "deletetrack").toString());
		return ResponseEntity.created(uri).body(null);
	}
	// --------------------
	// Artists
	// --------------------

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
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		artistController.deleteArtist(artistId, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	@PutMapping("EditMyArtist")
	public ResponseEntity<ArtistsModel> editMyArtist(@RequestBody ArtistsEditForm newInformation,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "editMyArtist").toString());
		return ResponseEntity.created(uri).body(artistController.editArtistInfo(newInformation, request));
	}

	// --------------------
	// Artist in track
	// --------------------

	@GetMapping("IsAnyInThisTrack")
	public ResponseEntity<Boolean> isAnyInThisTrack(@RequestParam(required = true) int trackId) {
		return ResponseEntity.ok().body(artistController.checkIfArtistsExistInTrack(trackId));
	}

	@GetMapping("ListArtistInTrack")
	public ResponseEntity<Page<ArtistsModel>> listArtistInTrack(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(required = true) int trackId) {
		return ResponseEntity.ok().body(artistController.listArtistInCurrentTrack(page, pageSize, trackId));
	}

	@PutMapping("EditMyArtistInTrack")
	public ResponseEntity<ArtistsTrackModel> editMyArtistInTrack(
			@RequestBody(required = true) ArtistTrackForm newInformation, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "editMyArtistInTrack").toString());
		return ResponseEntity.created(uri).body(artistController.editArtistTrackDescription(newInformation, request));
	}

	@PostMapping("AddMyArtistToTrack")
	public ResponseEntity<ArtistsTrackModel> addMyArtistToTrack(
			@RequestBody(required = true) ArtistTrackForm newArtistInTrack, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(artistController.addArtistToTrack(newArtistInTrack, request));
	}

	@DeleteMapping("DeleteMyArtistFromTrack")
	public ResponseEntity<HttpStatus> deleteMyArtistFromTrack(@RequestBody(required = true) ArtistTrackForm targetForm,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		artistController.DeleteArtistFromTrack(targetForm, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

}
