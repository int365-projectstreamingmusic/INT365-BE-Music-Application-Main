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

	@Query(value = "SELECT r FROM ReportGroupModel r WHERE r.startedBy = :userId ORDER BY r.recentDate DESC")
	Page<ReportGroupModel> listReportGroupByOwner(Pageable pageable, int userId);

	@Query(value = "SELECT r FROM ReportGroupModel r ORDER BY r.recentDate DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable);

	@Query(value = "SELECT r FROM ReportGroupModel r WHERE r.title LIKE LOWER(CONCAT('%',:searchKey,'%')) ORDER BY r.recentDate DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable, String searchKey);

	@Query(nativeQuery = true, value = "SELECT * FROM report_group "
			+ "WHERE group_name LIKE LOWER(CONCAT('%',:searchKey,'%')) "
			+ "AND type_id LIKE LOWER(CONCAT('%',:typeId,'%')) ORDER BY recent_date DESC")
	Page<ReportGroupModel> listReportGroup(Pageable pageable, String searchKey, int typeId);

	@Query(nativeQuery = true, value = "SELECT * FROM report_group WHERE recent_date = (SELECT MIN(recent_date) FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId) AND ref_id = :targetRef AND type_id = :typeId")
	ReportGroupModel getOldestReportGroup(int typeId, int targetRef);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId AND startedBy = :userId)")
	int existsByUserIdAndRefAndType(int userId, int typeId, int targetRef);

	@Query(nativeQuery = true, value = "SELECT * FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId AND startedBy = :userId")
	ReportGroupModel getByUserIdAndRefAndType(int userId, int typeId, int targetRef);

	@Query(nativeQuery = true, value = "SELECT * FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId AND is_solved = 0")
	ReportGroupModel getByRefIdAndType(int typeId, int targetRef);

	@Query(nativeQuery = true, value = "SELECT COUNT(r.id) FROM (SELECT id FROM report_group WHERE ref_id = :targetRef AND type_id = :typeId) r")
	int numberOfReportToTarget(int targetRef, int typeId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "DELETE FROM report_group WHERE id = :id")
	void deleteRecordById(int id);

	@Transactional
	@Modifying
	@Query(value = "UPDATE ReportGroupModel SET isSolved = :isSolved WHERE id = :id")
	void updateIsSolved(int id, boolean isSolved);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "UPDATE report_group SET recent_date = :newDate WHERE id = :id")
	void updateCurrentDate(int id, String newDate);

}
