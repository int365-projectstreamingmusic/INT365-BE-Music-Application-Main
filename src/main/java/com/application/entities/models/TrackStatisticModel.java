package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.application.entities.copmskeys.TrackCountCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "track_count_statistic", schema = "sitgarden")
public class TrackStatisticModel {

	@Id
	private TrackCountCompKey id;

	@Column(name = "view_count")
	private int viewCount;

	@Column(name = "favorite_count")
	private int favoriteCount;

}
