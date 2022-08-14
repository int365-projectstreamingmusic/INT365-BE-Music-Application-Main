package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.UserAccountModel;

@Repository
public interface UserAccountModelRepository extends JpaRepository<UserAccountModel, Integer> {
	UserAccountModel findByUsername(String username);

	UserAccountModel findByEmail(String email);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);
	
	@Query(value = "SELECT u FROM UserAccountModel u WHERE u.first_name LIKE %?1% AND u.last_name LIKE %?2% ")
	boolean existsByFirst_NameIgnoreCaseAndExistsByLast_NameIgnoreCase(String first_name, String last_name);
	
	@Query(value = "SELECT u FROM UserAccountModel u WHERE u.username LIKE %?1% OR u.email LIKE %?1% ORDER BY u.account_id DESC")
	Page<UserAccountModel> findByUsernameContainingOrEmailContaining(String searchContent, Pageable pageable);
	
	
}
