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
public class MoodPlaylistCompKey implements Serializable {

	@Column(name = "playlist_id")
	private int playlistId;

	@Column(name = "mood_id")
	private int moodId;

}
