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

	@Query(value = "UPDATE TracksModel t SET t.trackFile = :trackName WHERE t.id = :trackId")
	@Transactional
	@Modifying
	int updateTrackFileName(String trackName, int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE tracks SET track_name = :newTrackName, track_desc = :newTrackDesc WHERE track_id = :trackId")
	void updateBasicTrackInfo(int trackId, String newTrackName, String newTrackDesc);

}
