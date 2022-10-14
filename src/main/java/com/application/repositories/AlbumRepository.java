package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.AlbumModel;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Integer> {

}
