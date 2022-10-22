package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.AlbumModel;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM album WHERE LOWER(album_name) LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<AlbumModel> listAll(String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM album WHERE LOWER(album_name) LIKE LOWER(CONCAT('%',:searchContent,'%')) AND account_id = :userId")
	Page<AlbumModel> listAll(String searchContent, int userId, Pageable pageable);
	
}
