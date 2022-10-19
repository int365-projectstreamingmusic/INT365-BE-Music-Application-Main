package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.MoodTrackCompKey;
import com.application.entities.models.MoodTrackModel;

@Repository
public interface MoodTrackRepository extends JpaRepository<MoodTrackModel, MoodTrackCompKey> {

}