package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.ViewCountCompKey;
import com.application.entities.models.ViewCountModel;

@Repository
public interface ViewCountRepository extends JpaRepository<ViewCountModel, ViewCountCompKey> {

	@Query(value = "UPDATE ViewCountModel v SET v.viewCount = :newCount WHERE v.id = :id")
	@Modifying
	@Transactional
	int updateViewCount(ViewCountCompKey id, int newCount);

	@Query(nativeQuery = true, value = "SELECT v FROM view_count v WHERE v.track_id = :trackId")
	List<ViewCountModel> listViewCountByTrackId(int trackId);

	// OK!
	@Query(nativeQuery = true, value = "SELECT CASE WHEN COUNT(v.view_count_date) > 0 THEN SUM(v.view_count) ELSE 0 END FROM view_count v WHERE v.track_id = :trackId")
	int getAllViewCountFromTrackId(int trackId);

	// ListTopNViewCountOfTheWeek
	// int listTopNViewCountOfTheWeek(String week, int viewCount);

	// insertNewWeekRecord
	// void insertNewWeekRecord(int viewCount,String date);

	/*
	 * @Query(value =
	 * "SELECT v FROM ViewCountModel v WHERE v.id.countDate = :currentWeek ORDER BY v.viewCount LIMIT :topN"
	 * ) List<ViewCountModel> getTopViewCountInWeek(int topN, String currentWeek);
	 */
}
