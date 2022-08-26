package com.application.apis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PropertySource("generalsetting.properties")
@RestController
@RequestMapping("api/general/")
public class GeneralApis {

	@Value("${minio.storage.user-profile}")
	String minioUserProfile;

	@Value("${minio.storage.music-thumbnail}")
	String minioMusicThumbnail;

	@Value("${minio.storage.playlist-thumbnail}")
	String minioPlaylistThumbnail;

	// GetTrackThumbnailImage
	@GetMapping("track-thumbnail/{imageName}")
	public ResponseEntity<Resource> getTrackThumbnailImage(@PathVariable String imageName) {
		return ResponseEntity.ok().body(null);
	}

	// GetProfileThumbnailImage
	@GetMapping("profile-thumbnail/{imageName}")
	public ResponseEntity<Resource> getProfileThumbnailImage(@PathVariable String imageName) {
		return ResponseEntity.ok().body(null);
	}

	// GetProfileThumbnailImage
	@GetMapping("playlist-thumbnail/{imageName}")
	public ResponseEntity<Resource> getPlaylistThumbnailImage(@PathVariable String imageName) {
		return ResponseEntity.ok().body(null);
	}
}
