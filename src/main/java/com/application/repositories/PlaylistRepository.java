package com.application.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.PlaylistModel;

@Repository

public interface PlaylistRepository extends JpaRepository<PlaylistModel, Integer> {

	// @Query(value = "SELECT p FROM playlist p WHERE p.account_id = ?1")
	// Page<PlaylistModel> findByUserAccountModel(int account_id, Pageable
	// pageable);

	//Optional<PlaylistModel> findByPlaylist_Id(int playlist_id);

	@Query("UPDATE PlaylistModel p SET p.playlist_desc = :desc WHERE p.playlist_id = :playlistId")
	@Transactional
	@Modifying
	PlaylistModel updatePlaylistDesc(int playlistId, String desc);

	@Query("UPDATE PlaylistModel p SET p.thumbnail = :thumbnail WHERE p.playlist_id = :playlistId")
	@Transactional
	@Modifying
	PlaylistModel updatePlaylistThumbnail(int playlistId, String thumbnail);

}
