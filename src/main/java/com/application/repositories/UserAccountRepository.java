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
public interface UserAccountRepository extends JpaRepository<UserAccountModel, Integer> {

	UserAccountModel findByUsername(String username);

	UserAccountModel findByEmail(String email);

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByUsernameIgnoreCase(String username);
	
	@Query(nativeQuery = true, value = "SELECT account_id FROM user_accounts WHERE username = ':username'")
	int getUserIdFromUserName(String username);
	
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

	@Query(value = "UPDATE UserAccountModel u SET u.profileName = :newProfileName WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserProfileName(String newProfileName, int accountId);

	@Query(value = "UPDATE UserAccountModel u SET u.firstName = :newFirstName WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserFirstName(String newFirstName, int accountId);

	@Query(value = "UPDATE UserAccountModel u SET u.lastName = :newLastName WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserLastName(String newLastName, int accountId);

	@Query(value = "UPDATE UserAccountModel u SET u.userBios = :newBio WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserBio(String newBio, int accountId);

	@Query(value = "UPDATE UserAccountModel u SET u.profileIamge = :fileName WHERE u.accountId = :accountId")
	@Transactional
	@Modifying
	int updateUserProfileImage(String fileName, int accountId);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM user_accounts WHERE account_id = :accountId)")
	int existsByAccountId(int accountId);
	
	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT * FROM user_accounts ua INNER JOIN user_roles ur ON ua.account_id = ur.account_id WHERE ur.roles_id = 2003 AND ur.account_id = :accountId)")
	int checkUserAccountIdIfSuspended(int accountId);

}
