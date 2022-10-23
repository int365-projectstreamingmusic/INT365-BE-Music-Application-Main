package com.application.apis;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.AlbumController;
import com.application.controllers.TrackStatisticController;
import com.application.entities.models.TrackCountModel;
import com.application.services.GeneralFunctionController;

@RestController
@RequestMapping("test/")
public class A0TestingApis {

	@Autowired
	private TrackStatisticController trackCountController;
	@Autowired
	private AlbumController albumController;

	@Autowired
	private GeneralFunctionController generalFunctionController;
	private static String mapping = "test/";

	// -----------------------
	// VIEW COUNT
	// -----------------------

	@PostMapping("testAlbum")
	public ResponseEntity<HttpStatus> addNewAlbum(@RequestParam String name, HttpServletRequest request) {
		albumController.createAlbum(name, generalFunctionController.getUserAccount(request));
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		return ResponseEntity.ok().body(HttpStatus.CREATED);
	}

	@GetMapping("AddCustomTrackCount")
	public ResponseEntity<List<TrackCountModel>> addCustomTrackCount(@RequestParam int trackId,
			@RequestParam int nDays) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(trackCountController.addCustomViewCount(trackId, nDays));
	}

	@PutMapping("addViewCount")
	public ResponseEntity<HttpStatus> increaseViewCount(@RequestParam int trackId) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addViewCount").toString());
		trackCountController.increateViewCount(trackId);
		return ResponseEntity.created(uri).body(HttpStatus.OK);
	}

	@GetMapping("getAllView")
	public ResponseEntity<String> getAllViewByTrackId(@RequestParam(required = true) int trackId) {
		return ResponseEntity.ok().body(" view = " + trackCountController.getViewCount(trackId));
	}

	@GetMapping("getViewFromDayTo")
	public ResponseEntity<Integer> getViewCountInThePastFewDay(@RequestParam(required = true) int trackId,
			@RequestParam(defaultValue = "7") int days) {
		return ResponseEntity.ok().body(trackCountController.getViewCountInPassDays(trackId, days));
	}
}
