package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.application.entities.copmskeys.MoodTrackCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mood_track", schema = "sitgarden")
public class MoodTrackModel {

	@EmbeddedId
	private MoodTrackCompKey id;

	@Column(name = "ratio")
	private int ratio;

	@ManyToOne
	@Transient
	@JoinColumn(name = "mood_id", referencedColumnName = "mood_id")
	private MoodModel mood;

	@ManyToOne
	@Transient
	@JoinColumn(name = "track_id", referencedColumnName = "track_id")
	private TracksModel track;
}
