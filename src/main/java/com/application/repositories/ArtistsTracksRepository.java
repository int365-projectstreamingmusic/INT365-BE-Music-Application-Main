package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.entities.copmskeys.ArtistTrackCompKey;
import com.application.entities.models.ArtistsTrackModel;

public interface ArtistsTracksRepository extends JpaRepository<ArtistsTrackModel, ArtistTrackCompKey> {

}
