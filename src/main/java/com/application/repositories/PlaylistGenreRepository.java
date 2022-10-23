package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.PlaylistGenreCompKey;
import com.application.entities.models.PlaylistGenreModel;

@Repository
public interface PlaylistGenreRepository extends JpaRepository<PlaylistGenreModel, PlaylistGenreCompKey> {

}
