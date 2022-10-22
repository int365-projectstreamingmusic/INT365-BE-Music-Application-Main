package com.application.apis;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.AlbumController;
import com.application.controllers.GenreController;
import com.application.controllers.MoodController;
import com.application.controllers.PlaylistController;
import com.application.controllers.TrackController;
import com.application.entities.models.AlbumModel;
import com.application.entities.models.GenreModel;
import com.application.entities.models.MoodModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;
import com.application.entities.submittionforms.PlaylistOutput;

@RestController
@RequestMapping("api/public/")
public class A1VisitorApi {

	@Autowired
	private TrackController trackController;
	@Autowired
	private PlaylistController playlistController;
	@Autowired
	private GenreController genreController;
	@Autowired
	private MoodController moodController;
	@Autowired
	private AlbumController albumController;

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK! : HttpServeletRequest
	// TRACK : Get track by page
	@GetMapping("track")
	public ResponseEntity<Page<TracksModel>> listTrackByPageAndName(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.listTrackByPageAndName(page, pageSize, searchContent, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK! : HttpServeletRequest
	// TRACK : Get track by ID
	@GetMapping("track/{id}")
	public ResponseEntity<TracksModel> getTrackById(@PathVariable int id, HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.getTrackById(id, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// TRACK : Get latest X tracks.
	@GetMapping("track/latest")
	public ResponseEntity<List<TracksModel>> listLatestReleaseByNumber(
			@RequestParam(defaultValue = "5") int numberOfTracks, HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.listLatestRelease(numberOfTracks, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// TRACK : Get the top track people listen in the past week.
	@GetMapping("track/topall")
	public ResponseEntity<List<TracksModel>> getTopTracks(@RequestParam(defaultValue = "5") int numberOfTracks,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.getTopTrackOfNDay(numberOfTracks, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// TRACK : Get the top track people listen in the past week.
	@GetMapping("track/top")
	public ResponseEntity<List<TracksModel>> getTopTracks(@RequestParam(defaultValue = "7") int days,
			@RequestParam(defaultValue = "5") int numberOfTracks, HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.getTopTrackOfNDay(numberOfTracks, days, request));
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// GENRE : Get all genre list
	@GetMapping("genres")
	public ResponseEntity<Page<GenreModel>> listGenre(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(genreController.listGenreListByPage(page, pageSize, searchContent));
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	/// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// MOOD : List all moods.
	@GetMapping("mood")
	public ResponseEntity<Page<MoodModel>> listMood(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(moodController.listMood(page, pageSize, searchContent));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// MOOD L get by ID
	@GetMapping("mood/{id}")
	public ResponseEntity<MoodModel> getMoodById(@PathVariable int id) {
		return ResponseEntity.ok().body(moodController.getMoodById(id));
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// ALBUM : Get all album.
	@GetMapping("album")
	public ResponseEntity<Page<AlbumModel>> listAlbum(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(albumController.listAlbum(page, pageSize, searchContent));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ALBUM : Get track in album.
	@GetMapping("album/{id}")
	public ResponseEntity<Page<TracksModel>> listAlbum(@PathVariable int id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackController.listTrackByAlbum(id, page, pageSize, searchContent, request));
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// PLAYLIST : Get play list by name.
	@GetMapping("playlist")
	public ResponseEntity<Page<PlaylistModel>> listPlaylist(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "") String searchContent) {
		return ResponseEntity.ok().body(playlistController.listAllPlaylist(page, pageSize, searchContent));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// PLAYLIST : Get latest 5 play list.
	@GetMapping("playlist/latest")
	public ResponseEntity<List<PlaylistModel>> listLatestPlaylist(
			@RequestParam(defaultValue = "5") int numberOfPlaylist) {
		return ResponseEntity.ok().body(playlistController.listLatestPlaylist(numberOfPlaylist));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5 OK!
	// PLAYLIST : Get playlist by ID and its tracks
	@GetMapping("playlist/{id}")
	public ResponseEntity<PlaylistOutput> getPlaylist(@PathVariable int id, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.getPlaylistByID(id, page, pageSize, searchContent, request));
	}

	// █████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████████
}
