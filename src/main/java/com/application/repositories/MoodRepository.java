package com.application.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.MoodModel;

@Repository
public interface MoodRepository extends JpaRepository<MoodModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM mood WHERE LOWER(mood) LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<MoodModel> listAll(String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT m.* FROM mood m RIGHT JOIN mood_playlist a ON m.id = a.mood_id WHERE a.playlist_id = :playlistId")
	List<MoodModel> listMoodByPlaylist(int playlistId);

	@Query(nativeQuery = true, value = "SELECT m.* FROM mood m RIGHT JOIN mood_track a ON m.id = a.mood_id WHERE a.track_id = :trackId")
	List<MoodModel> listMoodByTrack(int trackId);
	
	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM mood WHERE id = :id")
	int existsByMoodId(int id);
}
