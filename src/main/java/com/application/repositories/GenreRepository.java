package com.application.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.GenreModel;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Integer> {
	
	@Query(value = "SELECT g FROM GenreModel g WHERE g.genreName LIKE ?1%")
	Page<GenreModel> findByGenreName(String genreName, Pageable pageable);
	
	boolean existsByGenreName(String genreName);

}
