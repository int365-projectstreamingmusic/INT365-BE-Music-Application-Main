package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.models.GenresTracksModel;

@Repository
public interface GenresTracksRepository extends JpaRepository<GenresTracksModel, GenreTracksCompkey> {

	@Query(nativeQuery = true, value = "SELECT * FROM genre_tracks WHERE tracK_id = :trackId")
	List<GenresTracksModel> findAllByTrackId(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO genre_tracks VALUES(:trackId,:genreId)")
	void insertNewGenreTrack(int trackId, int genreId);
	
	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM genre_tracks WHERE track_id = :trackId)")
	int existsByTrackId(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM genre_tracks WHERE track_id = :trackId")
	void deleteGenreTrack(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM genre_tracks WHERE track_id = :trackId AND genre_id = :genreid")
	void deleteGenreTrack(int trackId, int genreid);

}
