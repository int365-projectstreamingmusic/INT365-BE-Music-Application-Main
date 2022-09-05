package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.ViewCountCompKey;
import com.application.entities.models.ViewCountModel;

@Repository
public interface ViewCountRepository extends JpaRepository<ViewCountModel, ViewCountCompKey> {

	@Query(value = "UPDATE ViewCountModel v SET v.viewCount = :newCount WHERE v.id = :id")
	@Modifying
	@Transactional
	int updateShit(ViewCountCompKey id, int newCount);

	/*@Query(value = "SELECT v FROM ViewCountModel v WHERE v.id.countDate = :currentWeek ORDER BY v.viewCount LIMIT :topN")
	List<ViewCountModel> getTopViewCountInWeek(int topN, String currentWeek);*/
}
