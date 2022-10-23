package com.application.entities.copmskeys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistGenreCompKey implements Serializable {

	@Column(name = "playlist_id")
	private int playlisId;

	@Column(name = "genre_id")
	private int genreId;

}
