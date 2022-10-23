package com.application.entities.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.PlaylistGenreCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "playlist_genre", schema = "sitgarden")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistGenreModel {

	@EmbeddedId
	private PlaylistGenreCompKey id;

}
