package com.application.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.entities.models.ArtistsModel;
import com.application.entities.models.GenreModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TrackMarkingModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.repositories.ArtistsRepository;
import com.application.repositories.ArtistsTracksRepository;
import com.application.repositories.GenreRepository;
import com.application.repositories.PlaylistRepository;
import com.application.repositories.TrackMarkingRepository;
import com.application.repositories.TracksRepository;
import com.application.repositories.UserAccountModelRepository;

@RestController
@RequestMapping("api/testing/")
public class TestingModelsOnly {

	@Autowired
	private UserAccountModelRepository userAccountModelRepository;
	@Autowired
	private ArtistsRepository artistsRepository;
	@Autowired
	private TrackMarkingRepository trackMarkingRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private PlaylistRepository playlistRepository;
	@Autowired
	private TracksRepository tracksRepository;

	@GetMapping("getUser")
	public ResponseEntity<List<UserAccountModel>> getUserAccount() {
		return ResponseEntity.ok().body(userAccountModelRepository.findAll());
	}

	@GetMapping("getArtists")
	public ResponseEntity<List<ArtistsModel>> getAllArtists() {
		return ResponseEntity.ok().body(artistsRepository.findAll());
	}

	@GetMapping("getTrackMarking")
	public ResponseEntity<List<TrackMarkingModel>> getTrackMarkingModel() {
		return ResponseEntity.ok().body(trackMarkingRepository.findAll());
	}

	@GetMapping("getGenre")
	public ResponseEntity<List<GenreModel>> getGenres() {
		return ResponseEntity.ok().body(genreRepository.findAll());
	}

	@GetMapping("playlist")
	public ResponseEntity<List<PlaylistModel>> getPlaylist() {
		return ResponseEntity.ok().body(playlistRepository.findAll());
	}

	@GetMapping("getTracks")
	public ResponseEntity<List<TracksModel>> getTracks() {
		return ResponseEntity.ok().body(tracksRepository.findAll());
	}


}
