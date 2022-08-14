package com.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.PlaylistTrackListCompkey;
import com.application.entities.models.PlaylistTrackListModel;

@Repository
public interface PlaylistTrackListRepository extends JpaRepository<PlaylistTrackListModel, PlaylistTrackListCompkey> {

}
