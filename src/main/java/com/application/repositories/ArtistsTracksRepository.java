package com.application.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.application.entities.copmskeys.ArtistTrackCompKey;
import com.application.entities.models.ArtistsTrackModel;

public interface ArtistsTracksRepository extends JpaRepository<ArtistsTrackModel, ArtistTrackCompKey> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE artist_tracks SET artist_description = :newDexcription WHERE track_id = :trackId AND artist_id = :artistId")
	void updateArtistTrackDescription(int trackId, int artistId, String newDexcription);
}
