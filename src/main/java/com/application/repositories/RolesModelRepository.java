package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.RolesModel;

@Repository
public interface RolesModelRepository extends JpaRepository<RolesModel, Integer> {

}
