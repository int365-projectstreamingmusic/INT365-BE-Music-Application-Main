package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.TrackCountCompKey;
import com.application.entities.models.TrackCountModel;

@Repository
public interface TrackCountRepository extends JpaRepository<TrackCountModel, TrackCountCompKey> {

	@Query(nativeQuery = true, value = "SELECT v FROM view_count v WHERE v.track_id = :trackId")
	List<TrackCountModel> listViewCountByTrackId(int trackId);

	// OK!
	@Query(nativeQuery = true, value = "SELECT CASE WHEN COUNT(v.view_count_date) > 0 THEN SUM(v.view_count) ELSE 0 END FROM view_count v WHERE v.track_id = :trackId")
	int getAllViewCountFromTrackId(int trackId);

	// UpdateViewCount
	@Modifying
	@Transactional
	@Query(value = "UPDATE TrackCountModel v SET v.viewCount = :newViewCount WHERE v.id = :id ")
	void updateViewCount(int newViewCount, TrackCountCompKey id);
	

	/*@Modifying
	@Transactional
	@Query
	void insertNewViewCountRecord();*/
}
