package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "track_marking", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrackMarkingModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int track_marking_id;
	
	private String track_marking_title;
	private String track_marking_desc;
}
