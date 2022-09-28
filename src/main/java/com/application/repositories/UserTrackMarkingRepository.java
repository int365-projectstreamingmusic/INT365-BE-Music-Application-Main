package com.application.repositories;

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
}
