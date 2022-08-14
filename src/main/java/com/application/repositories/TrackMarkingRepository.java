package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.TrackMarkingModel;

@Repository
public interface TrackMarkingRepository extends JpaRepository<TrackMarkingModel, Integer>{

}
