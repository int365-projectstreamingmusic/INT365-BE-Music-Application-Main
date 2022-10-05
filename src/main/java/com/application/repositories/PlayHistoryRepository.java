package com.application.repositories;

import java.sql.Timestamp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.PlayHistoryModel;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistoryModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM play_history WHERE account_id = :userId")
	Page<PlayHistoryModel> listHistoryByUserId(int userId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT p.history_id, p.timestamp, p.account_id, p.track_id FROM play_history p LEFT JOIN tracks t ON p.track_id = t.track_id WHERE p.account_id = :userId AND t.track_name LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<PlayHistoryModel> findHistoryByUserIdAndSearchName(int userId, String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM play_history WHERE account_id = :userId AND track_id = :trackId LIMIT 1")
	PlayHistoryModel findRecordByUserIdAndTrackId(int userId, int trackId);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM play_history WHERE account_id = :userId AND track_id = :trackId)")
	int isExistedRecord(int userId, int trackId);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM play_history WHERE account_id = :userId)")
	int hasAtLeastOneRecord(int userId);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM play_history WHERE account_id = :userId AND timestamp > :timeAfter)")
	int hasAtLeastOneRecordAfterTimeRange(int userId, Timestamp timeAfter);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO play_history (timestamp,account_id,track_id)VALUES(:timeStamp,:userId,:trackId)")
	void insertNewPlayHistory(int userId, int trackId, String timeStamp);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM play_history WHERE account_id = :userId")
	void deleteAllByUserAccountId(int userId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM play_history WHERE account_id = :userId AND timestamp > :timeAfter")
	void deleteAllByUserAccountIdAndTimeRange(int userId, Timestamp timeAfter);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM play_history WHERE account_id = :userId AND track_id = :trackId ")
	void deleteByUserIdAndTrackId(int userId, int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE play_history SET timestamp = :newStamp WHERE account_id = :userId AND track_id = :trackId")
	void updateTimeStamp(Timestamp newStamp, int userId, int trackId);

}
