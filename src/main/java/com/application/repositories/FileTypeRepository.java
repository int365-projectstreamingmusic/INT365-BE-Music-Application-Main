package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.FileTypeModel;

@Repository
public interface FileTypeRepository extends JpaRepository<FileTypeModel, Integer> {

}
