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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.AlbumController;
import com.application.controllers.ArtistController;
import com.application.controllers.TrackController;
import com.application.controllers.TrackStatisticController;
import com.application.controllers.TrackGeneralController;
import com.application.controllers.TrackMarkingController;
import com.application.entities.models.AlbumModel;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.ArtistsTrackModel;
import com.application.entities.models.TracksModel;
import com.application.entities.submittionforms.AlbumForm;
import com.application.entities.submittionforms.ArtistTrackForm;
import com.application.entities.submittionforms.ArtistsEditForm;
import com.application.entities.submittionforms.TrackForm;
import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("/api/artist/")
public class A3CreatorApis {

	@Autowired
	private AlbumController albumController;
	@Autowired
	private TrackGeneralController trackGeneralController;
	@Autowired
	private ArtistController artistController;
	@Autowired
	private TrackController trackController;

	private static String mapping = "/api/artist/";

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ALBUM : List my created album.
	@GetMapping("album")
	public ResponseEntity<Page<AlbumModel>> getMyAlbum(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(albumController.myAlbum(page, pageSize, searchContent, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ALBUM : Create album
	@PostMapping("album")
	public ResponseEntity<AlbumModel> createAlbum(@RequestBody AlbumForm form, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "album").toString());
		return ResponseEntity.created(uri).body(albumController.createAlbum(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ALBUM : Edit album
	@PutMapping("album")
	public ResponseEntity<AlbumModel> editAlbum(@RequestBody AlbumForm form, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "album").toString());
		return ResponseEntity.created(uri).body(albumController.editAlbum(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ALBUM : Delete album
	@DeleteMapping("album")
	public ResponseEntity<HttpStatus> deleteAlbum(@RequestParam(required = true) int albumId,
			HttpServletRequest request) {
		albumController.deleteAlbum(albumId, request);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "album").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// --------------------
	// Track
	// --------------------

	// DB-V5 OK!
	@GetMapping("track")
	public ResponseEntity<Page<TracksModel>> getMyTrack(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackGeneralController.listMyTrack(page, pageSize, searchContent, request));
	}

	// DB-V5 OK!
	@PostMapping("track")
	public ResponseEntity<TracksModel> createNewTrack(
			@RequestPart(required = true, name = "newTrack") TrackForm newTrack,
			@RequestPart(required = true, name = "trackFile") MultipartFile trackFile,
			@RequestPart(required = false, name = "imageFile") MultipartFile imageFile, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "track").toString());
		return ResponseEntity.created(uri).body(trackController.addNewTrack(newTrack, trackFile, imageFile, request));
	}

	// DB-V5 OK!
	@PutMapping("track/edit")
	public ResponseEntity<TracksModel> editTrack(@RequestPart TrackForm track, @RequestPart MultipartFile image,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "track/edit").toString());
		return ResponseEntity.created(uri).body(trackController.editTrack(track, image, request));
	}

	// DB-V5 OK!
	@PutMapping("track/status")
	public ResponseEntity<String> changeStatus(@RequestParam(required = true) int trackId, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "track/status").toString());
		return ResponseEntity.created(uri).body(trackController.switchTrackStatus(trackId, request));
	}

	// DB-V5 OK!
	// DANGEROUS METHOD
	@DeleteMapping("track/delete")
	public ResponseEntity<String> deleteMyTrack(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "track/delete").toString());
		trackController.deleteTrack(trackId, request);
		return ResponseEntity.created(uri).body("Deleted");
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
