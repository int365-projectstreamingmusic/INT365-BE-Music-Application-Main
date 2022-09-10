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

	@Query(value = "SELECT CASE WHEN count(u) > 0 THEN true ELSE false END FROM UserAccountModel u WHERE LOWER(u.firstName) LIKE LOWER(concat('%',:firstName,'%')) AND LOWER(u.lastName) LIKE LOWER(CONCAT('%',:lastName,'%'))")
	boolean existByFirstnameOrLastname(String firstName, String lastName);

	@Query(value = "SELECT u FROM UserAccountModel u WHERE u.username LIKE %?1% OR u.email LIKE %?1% ORDER BY u.accountId DESC")
	Page<UserAccountModel> findByUsernameContainingOrEmailContaining(String searchContent, Pageable pageable);

	@Query(value = "UPDATE UserAccountModel u SET u.userPasscode = :newPassword WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserPassword(String newPassword, int accountId);

	@Query(value = "SELECT u FROM UserAccountModel u WHERE u.accountId = :id AND u.username = :username ")
	UserAccountModel findUserAccoutByNameAndId(int id, String username);

/*	@Query(value = "SELECT u FROM UserAccountModel u")
	@Transactional
	@Modifying
	int updateUserFirstName(String newFirstName, int id);

	@Query(value = "")
	@Transactional
	@Modifying
	int updateUserLastName(String newLastName, int id);*/
}
