package com.application.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.GenreModel;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Integer> {

	@Query(value = "SELECT g FROM GenreModel g WHERE g.genreName LIKE ?1%")
	Page<GenreModel> findByGenreName(String genreName, Pageable pageable);

	boolean existsByGenreName(String genreName);

	@Query(nativeQuery = true, value = "SELECT g.* FROM genre g RIGHT JOIN genre_tracks t ON g.genre_id = t.genre_id WHERE t.track_id = :trackId")
	List<GenreModel> findGenreByTrackId(int trackId);

	@Query(nativeQuery = true, value = "SELECT g.* FROM genre g RIGHT JOIN playlist_genre p ON g.genre_id = p.genre_id WHERE p.playlist_id = :playlistId")
	List<GenreModel> fineGenreByPlaylistId(int playlistId);

}
