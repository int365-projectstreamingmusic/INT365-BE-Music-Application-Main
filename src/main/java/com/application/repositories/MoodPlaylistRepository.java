package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.MoodPlaylistCompKey;
import com.application.entities.models.MoodPlaylistModel;

@Repository
public interface MoodPlaylistRepository extends JpaRepository<MoodPlaylistModel, MoodPlaylistCompKey> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE MoodPlaylistModel m SET m.count = :newCount WHERE m.id = :id")
	void updateCount(MoodPlaylistCompKey id, int newCount);

}
