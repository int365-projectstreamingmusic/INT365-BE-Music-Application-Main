package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.TracksModel;

@Repository
public interface TracksRepository extends JpaRepository<TracksModel, Integer> {

}
