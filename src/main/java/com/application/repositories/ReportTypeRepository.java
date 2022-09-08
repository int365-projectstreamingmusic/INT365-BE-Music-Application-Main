package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ReportTypeModel;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportTypeModel, Integer> {

}
