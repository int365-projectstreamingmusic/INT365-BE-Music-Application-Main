package com.application.apis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.TrackGeneralController;
import com.application.entities.models.TracksModel;

@RestController
@RequestMapping("/api/artist/")
public class ArtistApis {
	@Autowired
	private TrackGeneralController trackGeneralController;

	// --------------------
	// Track
	// --------------------

	@GetMapping("tracks")
	public ResponseEntity<Page<TracksModel>> getMyTrack(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackGeneralController.ListMyTrack(page, pageSize, searchContent, request));
	}

	@PutMapping("changeStatus")
	public void changeStatus(@RequestParam(required = true) int trackId, HttpServletRequest request) {

	}

	// DANGEROUS METHOD
	@DeleteMapping("delete-track")
	public void deleteMyTrack(@RequestParam(required = true) int trackId, HttpServletRequest request) {

	}

}
