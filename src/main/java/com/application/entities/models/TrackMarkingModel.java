package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "track_marking", schema = "sitgarden")
public class TrackMarkingModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int track_marking_id;

	@Column(name = "track_marking_title")
	private String title;

	@Column(name = "track_marking_desc")
	private String desc;

}
