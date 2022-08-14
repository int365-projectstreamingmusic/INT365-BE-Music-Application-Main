package com.application.entities.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.GenreTracksCompkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "genre_tracks", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class GenresTracksModel {
	@EmbeddedId
	private GenreTracksCompkey id;
}
