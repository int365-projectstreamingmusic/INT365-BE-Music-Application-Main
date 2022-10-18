package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.MoodTrackCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mood_track", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodTrackModel {

	@EmbeddedId
	private MoodTrackCompKey id;

	@Column(name = "ratio")
	private int ratio;
}
