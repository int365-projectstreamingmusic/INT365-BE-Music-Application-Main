package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.PlaylistGenreCompKey;
import com.application.entities.models.GenrePlaylistModel;

@Repository
public interface GenrePlaylistRepository extends JpaRepository<GenrePlaylistModel, PlaylistGenreCompKey> {

	@Query(nativeQuery = true, value = "SELECT * FROM genre_playlist WHERE playlist_id = :playlistId")
	List<GenrePlaylistModel> findAllByTrackId(int playlistId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO genre_playlist VALUES(:playlistId,:genreId)")
	void insertNewGenrePlaylist(int playlistId, int genreId);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM genre_playlist WHERE playlist_id = :playlistId)")
	int existsByPlaylistId(int playlistId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM genre_playlist WHERE playlist_id = :playlistId")
	void deleteGenrePlaylist(int playlistId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM genre_playlist WHERE playlist_id = :playlistId AND genre_id = :genreid")
	void deleteGenrePlaylist(int playlistId, int genreid);

}
