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

	/*@Transactional
	@Modifying
	@Query(value = "INSERT INTO album (album_name,album_decription,status_id,account_id)VALUES(:name,:desc,:statusId,:userId)")
	void insertNewAlbum(String name, String desc, int statusId, int userId);
*/
	/*
	 * @Query(value =
	 * "UPDATE TracksModel t SET t.trackFile = :trackName WHERE t.id = :trackId")
	 * 
	 * @Transactional
	 * 
	 * @Modifying int update(String trackName, int trackId);
	 */

}
