package com.application.apis;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.TrackManagerController;
import com.application.entities.models.TracksModel;
import com.application.entities.submittionforms.AddNewTrackForm;

@RestController
@RequestMapping("api/manager/track/")
public class ManagerTrackManagementApis {

	@Autowired
	private TrackManagerController trackManagerController;

	// AddNewTrack
	@PostMapping("new-track")
	public ResponseEntity<TracksModel> addNewTrack(@RequestPart AddNewTrackForm newTrack,
			@RequestPart MultipartFile trackFile, @RequestPart MultipartFile imageFile, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/member/track/newtrack").toString());
		return ResponseEntity.created(uri)
				.body(trackManagerController.addNewTrack(newTrack, trackFile, imageFile, request));
	}

	// RemoveTrack
	@DeleteMapping("remove-track")
	public ResponseEntity<String> removeTrack(int trackId, HttpServletRequest request) {
		return null;
	}

	// EditTrackInformation
	@PutMapping("edit-track")
	public ResponseEntity<TracksModel> editTrackInformation(@RequestPart AddNewTrackForm newTrackInfo,
			HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("api/member/track/editdescription").toString());
		return ResponseEntity.created(uri).body(null);
	}

	@GetMapping("")
	public String whyThis() {
		return "fuckl";
	}
}
