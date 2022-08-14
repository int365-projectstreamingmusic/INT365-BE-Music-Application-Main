package com.application.entities.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.ArtistTrackCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="artist_tracks", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ArtistsTrackModel {
	
	@EmbeddedId
	private ArtistTrackCompKey artistTrackID;
	
	private String artist_description;
}
