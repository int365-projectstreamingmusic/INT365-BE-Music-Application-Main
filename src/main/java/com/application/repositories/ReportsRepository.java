package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.ReportModel;

@Repository
public interface ReportsRepository extends JpaRepository<ReportModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM reports WHERE report_group_id = :reportGroupId ORDER BY report_date desc")
	List<ReportModel> getReportList(int reportGroupId);

	@Query(nativeQuery = true, value = "SELECT * FROM reports WHERE report_group_id = :reportGroupId ORDER BY report_date desc LIMIT :limit")
	List<ReportModel> getReportList(int reportGroupId, int limit);


}
