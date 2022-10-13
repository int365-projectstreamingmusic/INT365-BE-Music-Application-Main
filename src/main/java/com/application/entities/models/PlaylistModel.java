package com.application.entities.models;

import javax.persistence.Column;
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

@Table(name = "playlist", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlaylistModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playlist_id")
	private int id;

	@Column(name = "playlist_name")
	private String playlistName;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "playlist_desc")
	private String playlistDesc;

	private String thumbnail;

	@ManyToOne
	@JoinColumn(name = "account_id", updatable = false, insertable = false)
	private UserAccountModel userAccountModel;

	@ManyToOne
	@JoinColumn(name = "status_id", updatable = false, insertable = false)
	private PlayTrackStatusModel playTrackStatus;

}
