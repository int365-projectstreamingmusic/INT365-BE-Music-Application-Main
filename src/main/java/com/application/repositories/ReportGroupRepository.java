package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ReportGroupModel;

@Repository
public interface ReportGroupRepository extends JpaRepository<ReportGroupModel, Integer> {

}
