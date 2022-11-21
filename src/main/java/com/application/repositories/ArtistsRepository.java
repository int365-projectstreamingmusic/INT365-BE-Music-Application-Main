package com.application.repositories;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ArtistsModel;

@Repository
public interface ArtistsRepository extends JpaRepository<ArtistsModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT a FROM artists a WHERE a.added_by = :userAccountId")
	Page<ArtistsModel> listArtistByUserId(int userAccountId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM artists WHERE added_by = :userAccountId AND artist_name LIKE LOWER(CONCAT('%',:searchName,'%'))")
	Page<ArtistsModel> listArtistByUserIdAndArtistName(int userAccountId, String searchName, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM artists WHERE AND artist_name LIKE LOWER(CONCAT('%',:searchName,'%'))")
	Page<ArtistsModel> listArtistByArtistName(String searchName, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT a.* FROM (artists a RIGHT JOIN artist_tracks ar ON a.artist_id = ar.artist_id) LEFT JOIN tracks t ON t.track_id = ar.track_id WHERE t.track_id = :trackId")
	Page<ArtistsModel> listArtistInCurrentTrack(int trackId, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT * FROM artists WHERE artist_name = :artistName")
	ArtistsModel getArtistByName(String artistName);

	@Query(nativeQuery = true, value = "SELECT EXISTS(SELECT a.* FROM (artists a RIGHT JOIN artist_tracks ar ON a.artist_id = ar.artist_id) LEFT JOIN tracks t ON t.track_id = ar.track_id WHERE t.track_id = :trackId)")
	int isExistInTrackId(int trackId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO artists (artist_name,artist_bio,added_by)VALUES(:artistName,:artistDesc,:addedBy)")
	void insertNewArtist(String artistName, String artistDesc, int addedBy);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ArtistsModel a WHERE a.artistId = :artistId")
	void deleteByArtistId(int artistId);

	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "UPDATE artists SET artist_name = :newName, artist_bio = :newBio WHERE artist_id = :artistId")
	void updateArtistDetail(int artistId, String newName, String newBio);

}
