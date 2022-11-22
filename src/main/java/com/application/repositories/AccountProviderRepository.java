package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.models.AccountProviderModel;

@Repository
public interface AccountProviderRepository extends JpaRepository<AccountProviderModel, Integer> {

}
