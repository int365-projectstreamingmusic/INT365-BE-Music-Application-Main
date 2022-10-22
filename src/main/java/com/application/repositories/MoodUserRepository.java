package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.application.entities.copmskeys.MoodTrackCompKey;
import com.application.entities.models.MoodUserModel;

@Repository
public interface MoodUserRepository extends JpaRepository<MoodUserModel, MoodTrackCompKey> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE MoodUserModel m SET m.count = :newCount WHERE m.id = :id")
	void updateCount(MoodTrackCompKey id, int newCount);

}
