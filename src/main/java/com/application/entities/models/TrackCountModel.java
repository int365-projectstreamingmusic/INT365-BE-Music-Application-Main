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
@Table(name = "track_count_statistic", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class TrackCountModel {

	@Id
	private TrackCountCompKey id;

	@Column(name = "view_count")
	private int viewCount;
	
	@Column(name = "favorite_count")
	private int favoriteCount;

}
