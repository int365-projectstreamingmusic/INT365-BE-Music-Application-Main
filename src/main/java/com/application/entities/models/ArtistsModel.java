package com.application.entities.models;

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

@Table(name = "artists", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ArtistsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int artist_id;

	private String artist_name;
	private String artist_bio;

	@ManyToOne
	@JoinColumn(name = "added_by", insertable = false, updatable = false)
	private UserAccountModel userAccount;
}
