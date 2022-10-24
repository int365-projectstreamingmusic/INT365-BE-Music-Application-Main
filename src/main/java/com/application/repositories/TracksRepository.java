package com.application.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.TracksModel;

@Repository
public interface TracksRepository extends JpaRepository<TracksModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM tracks WHERE status_id = 1001 ORDER BY timestamp DESC LIMIT :numberOfTrack")
	List<TracksModel> listLatsestRelease(int numberOfTrack);

	@Query(nativeQuery = true, value = "SELECT * FROM tracks WHERE status_id = 1001 AND LOWER(track_name) LIKE LOWER(CONCAT('%',:searchContent,'%')) ORDER BY timestamp DESC")
	Page<TracksModel> findByTrackName(String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM tracks WHERE account_id = :accountId ORDER BY timestamp DESC")
	Page<TracksModel> listMyTrack(int accountId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM tracks WHERE account_id = :accountId AND track_name LIKE LOWER(CONCAT('%',:searchName,'%')) ORDER BY timestamp DESC")
	Page<TracksModel> listMyTrackByName(int accountId, String searchName, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT SUM(s.view_count) AS view_count, t.* FROM tracks t RIGHT JOIN track_count_statistic s ON t.track_id = s.track_id WHERE t.status_id = 1001 GROUP BY t.track_id ORDER BY view_count DESC LIMIT :numberOfTrack")
	List<TracksModel> listTopTrack(int numberOfTrack);

	@Query(nativeQuery = true, value = "SELECT SUM(s.view_count) AS view_count, t.* FROM tracks t RIGHT JOIN track_count_statistic s ON t.track_id = s.track_id WHERE s.view_count_date BETWEEN :from AND :to AND t.status_id = 1001 GROUP BY t.track_id ORDER BY view_count DESC LIMIT :numberOfTrack ")
	List<TracksModel> listTopTrack(int numberOfTrack, String from, String to);

	@Query(nativeQuery = true, value = "SELECT track_id FROM tracks WHERE track_file = :file")
	int getIdFromFileName(String file);

	@Query(value = "UPDATE TracksModel t SET t.trackFile = :trackName WHERE t.id = :trackId")
	@Transactional
	@Modifying
	int updateTrackFileName(String trackName, int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET track_name = :newTrackName, track_desc = :newTrackDesc WHERE track_id = :trackId")
	void updateBasicTrackInfo(int trackId, String newTrackName, String newTrackDesc);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET status_id = :statusId WHERE track_id = :trackId")
	void updateTrackStatus(int trackId, int statusId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET track_thumbnail = :thumbnail WHERE track_id = :trackId")
	void updateTrackThumbnail(int trackId, String thumbnail);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET view_count = view_count + 1 WHERE track_id = :id")
	void increaseViewCount(int id);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET favorite_count = favorite_count + 1 WHERE track_id = :id")
	void increaseFavoriteCount(int id);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET favorite_count = favorite_count - 1 WHERE track_id = :id")
	void decreaseFavoriteCount(int id);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET album_id = :albumId WHERE track_id = :id")
	void updateTrackAlbum(int id, int albumId);

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬

	@Query(nativeQuery = true, value = "SELECT t.* FROM tracks t RIGHT JOIN track_album a ON a.track_id = t.track_id WHERE a.album_id = :albumId AND LOWER(t.track_name) LIKE LOWER(CONCAT('%',:searchContent,'%')) AND t.status_id = 1001")
	Page<TracksModel> listAllByAlbum(int albumId, String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT t.* FROM tracks t RIGHT JOIN playlist_tracklist p ON p.track_id = t.track_id WHERE p.playlist_id = :playlist AND LOWER(t.track_name) LIKE LOWER(CONCAT('%',:searchContent,'%')) AND t.status_id = 1001")
	Page<TracksModel> listAllByPlaylist(int playlist, String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT t.* FROM tracks t RIGHT JOIN playlist_tracklist p ON p.track_id = t.track_id WHERE p.playlist_id = :playlist AND LOWER(t.track_name) LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<TracksModel> listAllByPlaylistOwner(int playlist, String searchContent, Pageable pageable);
}
