package com.application.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.PlaylistModel;

@Repository

public interface PlaylistRepository extends JpaRepository<PlaylistModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM playlist WHERE status_id = 2001 AND LOWER(playlist_name) LIKE LOWER(CONCAT('%',:searchName,'%')) ORDER BY created_date DESC ")
	Page<PlaylistModel> listAllPlaylist(String searchName, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM playlist WHERE account_id = :userId AND LOWER(playlist_name) LIKE LOWER(CONCAT('%',:searchName,'%')) ORDER BY created_date DESC ")
	Page<PlaylistModel> listAllPlaylistOwnedBy(int userId, String searchName, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM playlist WHERE status_id = 2001 ORDER BY created_date DESC LIMIT :numberOfPlaylist")
	List<PlaylistModel> listLatestPlaylist(int numberOfPlaylist);

	@Query("UPDATE PlaylistModel p SET p.playlistDesc = :desc WHERE p.id = :playlistId")
	@Transactional
	@Modifying
	PlaylistModel updatePlaylistDesc(int playlistId, String desc);

	@Query("UPDATE PlaylistModel p SET p.thumbnail = :thumbnail WHERE p.id = :playlistId")
	@Transactional
	@Modifying
	PlaylistModel updatePlaylistThumbnail(int playlistId, String thumbnail);

}
