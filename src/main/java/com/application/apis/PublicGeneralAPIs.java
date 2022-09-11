package com.application.apis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.GenreController;
import com.application.controllers.TrackController;
import com.application.entities.models.GenreModel;
import com.application.entities.models.TracksModel;

@RestController
@RequestMapping("api/public/general/")
public class PublicGeneralAPIs {

	@Autowired
	private GenreController genreController;
	@Autowired
	private TrackController trackController;

	// GetWelcomePageObject
	@GetMapping("welcome")
	public ResponseEntity<Map<String, Object>> getWelcomePageObject() {
		Map<String, Object> result = new HashMap<>();
		result.put("newArrivals", ""); // New Track
		result.put("trackOfTheWeek", ""); // Top 5
		result.put("RandomTrack", "");
		result.put("GenreList", ""); // Browse by genre
		return ResponseEntity.ok().body(result);
	}

	// OK!
	// ListGenreByPage
	@GetMapping("getGere")
	public ResponseEntity<Page<GenreModel>> listGenreByPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size,
			@RequestParam(defaultValue = "", required = false) String searchContent) {
		return ResponseEntity.ok().body(genreController.listGenreListByPage(page, size, searchContent));
	}

	// TRACK
	// TRACK
	// TRACK

	// OK!
	// ListTrackByPageAndName
	@GetMapping("track")
	public ResponseEntity<Page<TracksModel>> listTrackByPageAndName(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(trackController.listTrackByPageAndName(page, pageSize, searchContent));

	}

	// OK!
	// GetTrackDetailByTrackId
	@GetMapping("track/{trackId}")
	public ResponseEntity<Map<String, Object>> getTrackDetailByTrackId(@PathVariable int trackId) {
		return ResponseEntity.ok().body(trackController.getTrackDetailById(trackId));
	}

	// ListTopArtists

	// ListTopTrack

	// ListNewTrack

	// ListNewArtist

	// ListTrackOfTheWeek

	// --TEST--

}
