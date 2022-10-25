package com.application.entities.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "album", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class AlbumModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "album_id")
	private int id;

	@Column(name = "album_name")
	private String albumName;

	@Column(name = "album_decription")
	private String albumDescription;

	@ManyToOne
	@JoinColumn(name = "status_id", referencedColumnName = "status_id")
	private PlayTrackStatusModel status;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private UserAccountModel owner;

	@Transient
	@JoinColumn(name = "album_id", referencedColumnName = "album_id", insertable = false, updatable = false)
	private List<TracksModel> tracks;

}
