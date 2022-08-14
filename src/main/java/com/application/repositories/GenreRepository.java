package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.GenreModel;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Integer> {

}
