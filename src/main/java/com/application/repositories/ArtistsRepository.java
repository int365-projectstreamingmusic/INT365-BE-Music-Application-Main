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
	Page<ArtistsModel> getArtistByUserAccountModel(int userAccountId, Pageable pageable);

	@Query(value = "UPDATE ArtistsModel a SET a.artistName = :newName WHERE a.artistId = :artistId")
	@Transactional
	@Modifying
	ArtistsModel updateArtistName(int artistId, String newName);

	@Query(value = "UPDATE ArtistsModel a SET a.artistBio = :newBIo WHERE a.artistId = :artistId")
	@Transactional
	@Modifying
	ArtistsModel updateArtistBio(int artistId, String newBIo);
	
}
