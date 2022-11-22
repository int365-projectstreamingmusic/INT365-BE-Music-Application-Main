package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ActionHistoryModel;

@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistoryModel, Integer> {

	@Query(value = "SELECT a FROM ActionHistoryModel a ORDER BY timestamp DESC")
	Page<ActionHistoryModel> listHistory(Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM action_history WHERE action_type_id = :statusId ORDER BY timestamp DESC")
	Page<ActionHistoryModel> listHistoryByStatusId(Pageable pageable, int statusId);

	@Query(nativeQuery = true, value = "SELECT * FROM action_history WHERE account_id = = :userId ORDER BY timestamp DESC")
	Page<ActionHistoryModel> listHistoryByUserId(Pageable pageable, int userId);

	@Query(nativeQuery = true, value = "SELECT * FROM action_history WHERE description LIKE LOWER(CONCAT('%',:desc,'%')) ORDER BY timestamp DESC")
	Page<ActionHistoryModel> listHistoryByDescripton(Pageable pageable, String desc);

}
