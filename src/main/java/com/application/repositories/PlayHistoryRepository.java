package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.PlayHistoryModel;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistoryModel, Integer> {

}
