package com.application.apis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.application.entities.models.ArtistsModel;
import com.application.entities.models.GenreModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TrackMarkingModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.repositories.ArtistsRepository;
import com.application.repositories.GenreRepository;
import com.application.repositories.PlaylistRepository;
import com.application.repositories.TrackMarkingRepository;
import com.application.repositories.TracksModelRepository;
import com.application.repositories.UserAccountModelRepository;
import com.application.utilities.MinioStorageService;

@RestController
@RequestMapping("api/testing/")
@PropertySource("generalsetting.properties")
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
	private TracksModelRepository tracksModelRepository;
	@Autowired
	private MinioStorageService minioStorageService;

	@Value("${minio.storage.track.music}")
	String minioTrackLocation;

	@Value("${minio.storage.music-thumbnail}")
	String minioThumbmailLocation;

	@DeleteMapping("3")
	public ResponseEntity<String> do3(@RequestParam String name) {
		return ResponseEntity.ok().body(minioStorageService.deleteObjectFromMinioByName(minioThumbmailLocation, name));
	}

	@PostMapping("2")
	public ResponseEntity<String> do2(MultipartFile file) {
		return ResponseEntity.ok().body(minioStorageService.uploadImageToStorage(file, minioThumbmailLocation));
	}

	@PostMapping("1")
	public ResponseEntity<String> do1(MultipartFile file) {
		return ResponseEntity.ok().body(minioStorageService.uploadTrackToStorage(file, minioTrackLocation));
	}

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
		return ResponseEntity.ok().body(tracksModelRepository.findAll());
	}

}
