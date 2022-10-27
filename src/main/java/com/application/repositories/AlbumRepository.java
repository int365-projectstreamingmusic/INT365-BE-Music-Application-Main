package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.models.AlbumModel;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM album WHERE LOWER(album_name) LIKE LOWER(CONCAT('%',:searchContent,'%'))")
	Page<AlbumModel> listAll(String searchContent, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM album WHERE LOWER(album_name) LIKE LOWER(CONCAT('%',:searchContent,'%')) AND account_id = :userId")
	Page<AlbumModel> listAll(String searchContent, int userId, Pageable pageable);

	AlbumModel findByAlbumName(String albumName);

	boolean existsByAlbumName(String albumName);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "UPDATE album SET album_name = :newName WHERE album_id = :id")
	void updateNewAlbumName(int id, String newName);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "UPDATE album SET album_name = :newDesc WHERE album_id = :albumId")
	void updateNewAlbumdesc(int albumId, String newDesc);

}
