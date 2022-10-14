package com.application.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.GenreController;
import com.application.controllers.PlaylistController;
import com.application.controllers.TrackController;
import com.application.controllers.TrackGeneralController;
import com.application.entities.models.GenreModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;

@RestController
@RequestMapping("api/public/")
public class A1VisitorApi {

	@Autowired
	private TrackGeneralController trackGeneralController;
	@Autowired
	private TrackController trackController;
	@Autowired
	private PlaylistController playlistController;
	@Autowired
	private GenreController genreController;

	// DB-V5 OK!
	// TRACK : Get the top track people listen in the past week.
	@GetMapping("track/topall")
	public ResponseEntity<List<TracksModel>> getTopTracks(@RequestParam(defaultValue = "5") int numberOfTracks) {
		return ResponseEntity.ok().body(trackController.getTopTrackOfNDay(numberOfTracks));
	}

	// DB-V5 OK!
	// TRACK : Get the top track people listen in the past week.
	@GetMapping("track/top")
	public ResponseEntity<List<TracksModel>> getTopTracks(@RequestParam(defaultValue = "7") int days,
			@RequestParam(defaultValue = "5") int numberOfTracks) {
		return ResponseEntity.ok().body(trackController.getTopTrackOfNDay(numberOfTracks, days));
	}

	// DB-V5 OK!
	// GENRE : Get all genre list
	@GetMapping("genres")
	public ResponseEntity<Page<GenreModel>> listGenre(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(genreController.listGenreListByPage(page, pageSize, searchContent));
	}

	// DB-V5 OK!
	// TRACK : Get latest X tracks.
	@GetMapping("latest")
	public ResponseEntity<List<TracksModel>> listLatestReleaseByNumber(
			@RequestParam(defaultValue = "5") int numberOfTrack) {
		return ResponseEntity.ok().body(trackGeneralController.listLatestRelease(numberOfTrack));
	}

	// DB-V5 OK!
	// PLAYLIST : Get play list by name.
	@GetMapping("playlist")
	public ResponseEntity<Page<PlaylistModel>> listPlaylist(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(playlistController.listAllPlaylist(page, pageSize, searchContent));
	}

	// DB-V5 OK!
	// PLAYLIST : Get latest 5 play list.
	@GetMapping("playlist/latest")
	public ResponseEntity<List<PlaylistModel>> listLatestPlaylist(
			@RequestParam(defaultValue = "5") int numberOfPlaylist) {
		return ResponseEntity.ok().body(playlistController.listLatestPlaylist(numberOfPlaylist));
	}
}
