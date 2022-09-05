package com.application.entities.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "artist_id", referencedColumnName = "artist_id")
	private List<ArtistsTrackModel> artistTracks;

	@ManyToOne
	@JoinColumn(name = "added_by", insertable = false, updatable = false)
	private UserAccountModel userAccount;
}
