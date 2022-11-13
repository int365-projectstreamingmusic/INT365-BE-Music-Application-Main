package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ActionTypeModel;

@Repository
public interface ActionTypeRepository extends JpaRepository<ActionTypeModel, Integer> {

}
