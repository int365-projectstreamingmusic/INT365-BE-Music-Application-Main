package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.models.ReportGenreModel;

@Repository
public interface ReportGenreRepository extends JpaRepository<ReportGenreModel, Integer> {

	@Query(nativeQuery = true, value = "SELECT * FROM report_genre WHERE id LIKE CONCAT(:typeId,'%')")
	List<ReportGenreModel> listGenres(int typeId);

	@Query(nativeQuery = true, value = "SELECT DISTINCT(g.report_genre) "
			+ "FROM (report_genre g RIGHT JOIN reports r ON r.report_genre_id = g.id) "
			+ "RIGHT JOIN report_group t ON r.report_group_id = t.id "
			+ "WHERE t.type_id = :typeId AND t.ref_id = :targetRef")
	List<String> listGenreReported(int typeId, int targetRef);

}
