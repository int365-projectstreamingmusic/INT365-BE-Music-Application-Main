package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.models.GenresTracksModel;

@Repository
public interface GenresTracksRepository extends JpaRepository<GenresTracksModel, GenreTracksCompkey> {

}
