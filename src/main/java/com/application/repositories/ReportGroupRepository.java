package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.ReportGroupModel;

@Repository
public interface ReportGroupRepository extends JpaRepository<ReportGroupModel, Integer> {

	@Query(value = "SELECT r FROM ReportGroupModel r ORDER BY r.recentDate DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable);

	@Query(value = "SELECT r FROM ReportGroupModel r WHERE r.title LIKE LOWER(CONCAT('%',:searchKey,'%')) ORDER BY r.recentDate DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable, String searchKey);

	@Query(nativeQuery = true, value = "SELECT * FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId ORDER BY recent_date DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable, int typeId, int targetRef);

	@Query(nativeQuery = true, value = "SELECT COUNT(r.id) FROM (SELECT id FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId) r")
	int numberOfReportToTarget(int targetRef, int typeId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM report_group WHERE id = :id")
	void deleteById(int id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ReportGroupModel SET isSolved = :isSolved")
	void updateIsSolved(boolean isSolved);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE report_group SET recent_date = :newDate WHERE id = :id")
	void updateCurrentDate(int id, String newDate);
}
