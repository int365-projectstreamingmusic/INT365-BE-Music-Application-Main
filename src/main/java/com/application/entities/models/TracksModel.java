package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tracks", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class TracksModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int track_id;

	private String track_file;
	private String track_name;

	@JsonFormat(pattern = "yyyy/mm/dd")
	private String timestamp;

	private int duration;

	private String track_desc;
	private String thumbnail;
	private int view_count;

	@ManyToOne
	@JoinColumn(name = "account_id", insertable = false, updatable = false)
	private UserAccountModel userAccountModel;

}
