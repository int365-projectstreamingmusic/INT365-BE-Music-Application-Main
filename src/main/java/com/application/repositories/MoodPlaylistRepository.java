package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.MoodPlaylistCompKey;
import com.application.entities.models.MoodPlaylistModel;

@Repository
public interface MoodPlaylistRepository extends JpaRepository<MoodPlaylistModel, MoodPlaylistCompKey> {

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM mood_playlist WHERE playlist_id = :playlistId)")
	int existsByPlaylistId(int playlistId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM mood_playlist WHERE playlist_id = :playlistId")
	void deleteMoodPlaylist(int playlistId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM mood_playlist WHERE playlist_id = :playlistId AND mood_id = :moodId")
	void deleteMoodPlaylist(int playlistId, int moodId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO mood_playlist (mood_id,playlist_id,ratio)VALUES(:moodId,:playlistId,1)")
	void insertMoodPlaylist(int playlistId, int moodId);

}
