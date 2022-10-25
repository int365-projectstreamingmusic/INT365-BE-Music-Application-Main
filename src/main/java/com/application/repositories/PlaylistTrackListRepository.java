package com.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entities.copmskeys.PlaylistTrackListCompkey;
import com.application.entities.models.PlaylistTrackListModel;

@Repository
public interface PlaylistTrackListRepository extends JpaRepository<PlaylistTrackListModel, PlaylistTrackListCompkey> {

	@Query(nativeQuery = true, value = "SELECT track_id FROM playlist_tracklist WHERE playlist_id = :id")
	List<Integer> listTrackNumberById(int id);

	@Query(nativeQuery = true, value = "SELECT *  FROM playlist_tracklist WHERE playlist_id = :id")
	List<PlaylistTrackListModel> listTrack(int id);

}
