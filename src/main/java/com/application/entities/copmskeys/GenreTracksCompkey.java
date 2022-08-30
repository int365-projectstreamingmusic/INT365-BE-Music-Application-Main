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
public class GenreTracksCompkey implements Serializable {

	@Column(name = "track_id")
	private int trackId;

	@Column(name = "genre_id")
	private int genreId;
}
