package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.MoodPlaylistCompKey;
import com.application.entities.models.MoodPlaylistModel;

@Repository
public interface MoodPlaylistRepository extends JpaRepository<MoodPlaylistModel, MoodPlaylistCompKey> {


}
