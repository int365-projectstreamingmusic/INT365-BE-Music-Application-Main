package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;
import com.application.entities.models.UserTrackMarkingModel;

@Repository
public interface UserTrackMarkingRepository extends JpaRepository<UserTrackMarkingModel, UserTrackMarkingCompkey> {

}
