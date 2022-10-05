package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;
import com.application.entities.models.UserTrackMarkingModel;

@Repository
public interface UserTrackMarkingRepository extends JpaRepository<UserTrackMarkingModel, UserTrackMarkingCompkey> {

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO user_track_marking VALUES(:trackId,:accountId,:markingId)")
	void insertNewMarking(int trackId, int accountId, int markingId);

	@Query(nativeQuery = true, value = "SELECT * FROM user_track_marking WHERE track_id = :trackId AND account_id = :accountId AND track_marking_id = :markingId")
	UserTrackMarkingModel findTrackMarkingByAllKeys(int trackId, int accountId, int markingId);

	@Query(value = "SELECT t FROM UserTrackMarkingModel t WHERE t.id = :compkey")
	UserTrackMarkingModel findTrackMarkingByCompKey(UserTrackMarkingCompkey compkey);

	boolean existsById(UserTrackMarkingCompkey id);

	// Nuke all fro playground
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM user_track_marking WhERE account_id = :accountId AND track_marking_id = :trackMarkingId")
	void deleteAllPlaygroundById(int accountId, int trackMarkingId);

	@Query(nativeQuery = true, value = "SELECT * FROM user_track_marking WHERE account_id = :accountId AND track_marking_id = :trackMarkingId")
	Page<UserTrackMarkingModel> listAllFromUserIdAndTrackMarkingId(int accountId, int trackMarkingId,
			Pageable pageable);

	// Search by name
	@Query(nativeQuery = true, value = "SELECT u.track_id,u.account_id,u.track_marking_id"
			+ " FROM user_track_marking u INNER JOIN tracks t ON u.track_id = t.track_id"
			+ " WHERE t.track_name LIKE LOWER(concat('%',:searchContent,'%')) "
			+ "AND u.account_id = :accountId AND u.track_marking_id = :trackMarkingId")
	Page<UserTrackMarkingModel> listAllFromUserIdAndTrackMarkingIdAndSearchName(int accountId, int trackMarkingId,
			Pageable pageable, String searchContent);
}