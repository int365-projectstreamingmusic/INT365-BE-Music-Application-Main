package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private UserAccountModel owner;

}
