package com.application.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.application.entities.copmskeys.ArtistTrackCompKey;
import com.application.entities.models.ArtistsTrackModel;

public interface ArtistsTracksRepository extends JpaRepository<ArtistsTrackModel, ArtistTrackCompKey> {

	@Query(nativeQuery = true, value = "SELECT * FROM artist_tracks WHERE track_id = :id")
	List<ArtistsTrackModel> listArtistByTrack(int id);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE artist_tracks SET artist_description = :desc WHERE track_id = :trackId AND artist_id = :artistId")
	void updateArtistTrackDescription(int trackId, int artistId, String desc);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO artist_tracks (artist_description,track_id,artist_id)VALUES(:desc,:trackId,:artistId)")
	void insertArtistTrack(int trackId, int artistId, String desc);
}
