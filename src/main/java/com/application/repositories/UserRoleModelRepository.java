package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.UserRolesCompKey;
import com.application.entities.models.UserRolesModel;

@Repository
public interface UserRoleModelRepository extends JpaRepository<UserRolesModel, UserRolesCompKey> {

}
