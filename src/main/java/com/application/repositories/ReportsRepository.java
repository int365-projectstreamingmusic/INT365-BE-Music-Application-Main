package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ReportModel;

@Repository
public interface ReportsRepository extends JpaRepository<ReportModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM reports WHERE report_group_id = :reportGroupId ORDER BY report_date desc")
	List<ReportModel> getReportList(int reportGroupId);

	@Query(nativeQuery = true, value = "SELECT * FROM reports WHERE report_group_id = :reportGroupId ORDER BY report_date desc LIMIT :limit")
	List<ReportModel> getReportList(int reportGroupId, int limit);

	@Query(nativeQuery = true, value = "SELECT EXISTS "
			+ "(SELECT * FROM reports r LEFT JOIN report_group g ON r.report_group_id = g.id "
			+ "WHERE g.is_solved = 0 AND g.type_id = :reportType "
			+ "AND g.ref_id = :targetRef AND r.reported_by = :userId )")
	int existByReport(int reportType, int targetRef, int userId);

	@Query(nativeQuery = true, value = "SELECT COUNT(id) "
			+ "FROM report_group r LEFT JOIN user_track_comments c ON c.track_comment_id = r.ref_id  "
			+ "WHERE r.type_id = 2001 AND r.is_solved = 0 AND r.ref_id = :refId")
	int getCountTrackCommentReports(int refId);

	@Query(nativeQuery = true, value = "SELECT COUNT(id) "
			+ "FROM report_group r LEFT JOIN user_playlist_comments c ON c.playlist_comment_id = r.ref_id "
			+ "WHERE r.type_id = 2002 AND r.is_solved = 0 AND r.ref_id = :refId")
	int getCountPlaylistCommentReports(int refId);

	@Query(nativeQuery = true, value = "SELECT COUNT(id) "
			+ "FROM report_group r LEFT JOIN tracks t ON t.track_id = r.ref_id "
			+ "WHERE r.type_id = 1001 AND r.is_solved = 0 AND r.ref_id = :refId")
	int getCountTrackReports(int refId);

}
