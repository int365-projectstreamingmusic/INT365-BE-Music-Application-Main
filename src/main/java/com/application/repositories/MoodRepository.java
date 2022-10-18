package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.MoodModel;

@Repository
public interface MoodRepository extends JpaRepository<MoodModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM mood WHERE LOWER(mood) LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<MoodModel> listAll(String searchContent, Pageable pageable);

}
