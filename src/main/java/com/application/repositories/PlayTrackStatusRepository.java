package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.PlayTrackStatusModel;

@Repository
public interface PlayTrackStatusRepository extends JpaRepository<PlayTrackStatusModel, Integer> {

}
