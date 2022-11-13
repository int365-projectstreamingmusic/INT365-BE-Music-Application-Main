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
@Table(name = "artists", schema = "sitgarden")
public class ArtistsModel {
	@Id
	@Column(name = "artist_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int artistId;

	@Column(name = "artist_name")
	private String artistName;

	@Column(name = "artist_bio")
	private String artistBio;
	
	@Column(name = "added_by")
	private int addedBy;

}
