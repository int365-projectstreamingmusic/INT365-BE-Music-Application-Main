package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.FileLinkRefModel;
import com.application.entities.models.FileTypeModel;

@Repository
public interface FileLinkRefRepository extends JpaRepository<FileLinkRefModel, String> {

	// OK!
	// findByTargetRefAndTypeModel
	@Query(value = "SELECT f FROM FileLinkRefModel f WHERE f.targetRef = :targetRef AND f.fileType = :fileType ")
	FileLinkRefModel findByTargetRefAndTypeModel(FileTypeModel fileType, int targetRef);

	// OK!
	// findByTargetRefAndTypeId
	@Query(nativeQuery = true, value = "SELECT * FROM file_link_ref WHERE type_id = :typeId AND target_ref = :targetRef")
	FileLinkRefModel findByTargetRefAndTypeId(int typeId, int targetRef);

	// deleteByTargetRefAndTypeId
	@Query(nativeQuery = true, value = "DELETE FROM file_link_ref WHERE type_id = :typeId AND target_ref = :targetRef")
	@Modifying
	@Transactional
	int deleteByTargetRefAndTypeId(int typeId, int targetRef);

}
