package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.MoodTrackCompKey;
import com.application.entities.models.MoodUserModel;

@Repository
public interface MoodUserRepository extends JpaRepository<MoodUserModel, MoodTrackCompKey> {

}
