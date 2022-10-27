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

@Table(name = "artists", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
	
	/*@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "artist_id", referencedColumnName = "artist_id")
	private List<ArtistsTrackModel> artistTracks;*/
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "added_by", insertable = false, updatable = false)
	private UserAccountModel userAccount;*/
}
