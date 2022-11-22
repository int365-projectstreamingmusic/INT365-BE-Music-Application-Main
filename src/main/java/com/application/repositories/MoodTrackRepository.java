package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.MoodTrackCompKey;
import com.application.entities.models.MoodTrackModel;

@Repository
public interface MoodTrackRepository extends JpaRepository<MoodTrackModel, MoodTrackCompKey> {

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM mood_track WHERE track_id = :trackId)")
	int existsByTrackId(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM mood_track WHERE track_id = :trackId")
	void deleteMoodTrack(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM mood_track WHERE track_id = :trackId AND mood_id = :moodId")
	void deleteMoodTrack(int trackId, int moodId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO mood_track (mood_id,track_id,ratio)VALUES(:moodId,:trackId,1)")
	void insertMoodTrack(int trackId, int moodId);

}
