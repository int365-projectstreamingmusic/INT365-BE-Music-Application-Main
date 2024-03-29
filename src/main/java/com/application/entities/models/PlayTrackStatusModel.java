package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playtrack_status", schema = "sitgarden")
public class PlayTrackStatusModel {

	@Id
	@Column(name = "status_id")
	private int id;

	@Column(name = "status_desc")
	private String statusDesc;
	
}
