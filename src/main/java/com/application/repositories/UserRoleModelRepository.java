package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.UserRolesCompKey;
import com.application.entities.models.UserRolesModel;

@Repository
public interface UserRoleModelRepository extends JpaRepository<UserRolesModel, UserRolesCompKey> {

	@Query(value = "SELECT case when count(u)> 0 then true else false end FROM UserRolesModel u WHERE u.id = :id")
	boolean existByUserIdAndRoleId(UserRolesCompKey id);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "INSERT INTO user_roles VALUES(:userId,:roleId)")
	void addRoleToUser(int userId, int roleId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM user_roles WHERE account_id = :userId AND roles_id = :roleId")
	void removeRoleFromUser(int userId, int roleId);

	// OK!
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM UserRolesModel u WHERE u.id = :id")
	void deleteByUserIdAndRoleId(UserRolesCompKey id);

	// OK!
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "INSERT INTO user_roles VALUES(:userId,:roleId)")
	void insertNewRecord(int userId, int roleId);

	// OK!
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "DELETE FROM user_roles u WHERE u.account_id = :userId")
	void deleteAllByUserId(int userId);
}
