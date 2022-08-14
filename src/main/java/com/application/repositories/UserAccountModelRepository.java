package com.application.repositories;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.UserAccountModel;

@Repository
public interface UserAccountModelRepository extends JpaRepository<UserAccountModel, Integer> {
	UserAccountModel findByUsername(String username);

	UserAccountModel findByEmail(String email);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);

	@Query(value = "SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM UserAccountModel u WHERE LOWER(u.first_name) LIKE LOWER(concat('%',:first_name,'%')) AND LOWER(u.last_name) LIKE LOWER(CONCAT('%',:last_name,'%'))")
	boolean existByFirstnameOrLastname(String first_name, String last_name);

	@Query(value = "SELECT u FROM UserAccountModel u WHERE u.username LIKE %?1% OR u.email LIKE %?1% ORDER BY u.account_id DESC")
	Page<UserAccountModel> findByUsernameContainingOrEmailContaining(String searchContent, Pageable pageable);

	@Query(value = "UPDATE UserAccountModel u SET u.user_passcode = :newPassword WHERE u.account_id = :accountId")
	@Transactional
	@Modifying
	UserAccountModel updateUserPassword(String newPassword, int accountId);

}
