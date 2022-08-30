package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.TracksModel;

@Repository
public interface TracksModelRepository extends JpaRepository<TracksModel, Integer> {
	
	@Query(value = "UPDATE TracksModel t SET t.viewCount = :newViewCount WHERE t.id = :id")
	int updateNewVIewCount(int newViewCount, int id);

}
