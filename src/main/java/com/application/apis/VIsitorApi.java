package com.application.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.TrackGeneralController;
import com.application.entities.models.TracksModel;

@RestController
@RequestMapping("api/public/")
public class VIsitorApi {
	private static String mapping = "api/public/";

	@Autowired
	private TrackGeneralController trackController;

	@GetMapping("latest")
	public ResponseEntity<List<TracksModel>> getLatestReleaseByNumber(
			@RequestParam(defaultValue = "5") int numberOfTrack) {
		return ResponseEntity.ok().body(trackController.listLatestRelease(numberOfTrack));
	}
}
