package com.application.entities.models;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.entities.copmskeys.PlaylistGenreCompKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genre_playlist", schema = "sitgarden")
public class GenrePlaylistModel {

	@JsonIgnore
	@EmbeddedId
	private PlaylistGenreCompKey id;

	@ManyToOne
	@JoinColumn(name = "genre_id", insertable = false, updatable = false)
	@Basic(optional = true)
	private GenreModel genre;

}
