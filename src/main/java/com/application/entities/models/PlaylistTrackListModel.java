package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.application.entities.copmskeys.PlaylistTrackListCompkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "playlist_tracklist", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlaylistTrackListModel {
	@EmbeddedId
	private PlaylistTrackListCompkey id;

	@Column(name = "place_in_list")
	private int placeInList;

	@Column(name = "is_skipped")
	private int isSkipped;
}
