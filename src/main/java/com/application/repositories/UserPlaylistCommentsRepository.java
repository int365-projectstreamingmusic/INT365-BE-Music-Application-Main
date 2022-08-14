package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.UserPlaylistCommentsModel;

@Repository
public interface UserPlaylistCommentsRepository extends JpaRepository<UserPlaylistCommentsModel, Integer> {

}
