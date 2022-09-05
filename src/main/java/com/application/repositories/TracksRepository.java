package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.TracksModel;

@Repository
public interface TracksRepository extends JpaRepository<TracksModel, Integer> {

	@Query(value = "UPDATE TracksModel t SET t.trackFile = :trackName WHERE t.id = :trackId")
	@Transactional
	@Modifying
	int updateTrackFileName(String trackName, int trackId);

}