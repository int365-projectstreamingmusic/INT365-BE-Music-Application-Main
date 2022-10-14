package com.application.apis;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.TrackCountController;
import com.application.entities.models.TrackCountModel;

@RestController
@RequestMapping("test/")
public class A0TestingApis {

	@Autowired
	private TrackCountController trackCountController;

	private static String mapping = "test/";

	// -----------------------
	// VIEW COUNT
	// -----------------------

	@GetMapping("AddCustomTrackCount")
	public ResponseEntity<List<TrackCountModel>> addCustomTrackCount(@RequestParam int trackId,
			@RequestParam int nWeek) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(trackCountController.addCustomViewCount(trackId, nWeek));
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

	@GetMapping("getKen")
	public ResponseEntity<Boolean> getKen(@RequestParam int value1, @RequestParam int value2,
			@RequestParam int value3) {
		if (value1 < value2 || value2 < value3 || value1 < value3) {
			return ResponseEntity.ok().body(true);
		} else {
			return ResponseEntity.ok().body(false);
		}
	}

}
