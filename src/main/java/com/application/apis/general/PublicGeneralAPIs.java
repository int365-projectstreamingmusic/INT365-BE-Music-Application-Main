package com.application.apis.general;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.GenreController;
import com.application.entities.models.GenreModel;

@RestController
@RequestMapping("api/public/general/")
public class PublicGeneralAPIs {

	@Autowired
	private GenreController genreController;

	// GetWelcomePageObject
	@GetMapping("welcome")
	public ResponseEntity<Map<String, Object>> getWelcomePageObject() {
		Map<String, Object> result = new HashMap<>();
		result.put("newArrivals", "");
		result.put("trackOfTheWeek", "");
		result.put("RandomTrack", "");
		result.put("GenreList", "");
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

	// ListTopArtists

	// ListTopTrack

	// ListNewTrack

	// ListNewArtist

	// ListTrackOfTheWeek

}
