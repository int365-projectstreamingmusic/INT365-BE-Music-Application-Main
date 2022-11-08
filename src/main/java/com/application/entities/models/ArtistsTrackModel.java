package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.entities.copmskeys.ArtistTrackCompKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artist_tracks", schema = "sitgarden")
public class ArtistsTrackModel {

	@EmbeddedId
	@JsonIgnore
	private ArtistTrackCompKey artistTrackID;

	@Column(name = "artist_description")
	private String artistDescription;

	@ManyToOne(optional = true)
	@JoinColumn(name = "artist_id", insertable = false, updatable = false)
	private ArtistsModel artistsModel;

}
