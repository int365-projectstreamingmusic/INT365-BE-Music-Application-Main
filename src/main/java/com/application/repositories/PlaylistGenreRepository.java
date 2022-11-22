package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.PlaylistGenreCompKey;
import com.application.entities.models.GenrePlaylistModel;

@Repository
public interface PlaylistGenreRepository extends JpaRepository<GenrePlaylistModel, PlaylistGenreCompKey> {

}
