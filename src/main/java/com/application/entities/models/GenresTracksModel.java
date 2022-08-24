package com.application.entities.models;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "genre_tracks", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class GenresTracksModel {

	@JsonIgnore
	@EmbeddedId
	private GenreTracksCompkey id;

	@ManyToOne
	@JoinColumn(name = "genre_id", insertable = false, updatable = false)
	@Basic(optional = true)
	private GenreModel genre;
}
