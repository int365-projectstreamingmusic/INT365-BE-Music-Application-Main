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
@Table(name = "genre", schema = "sitgarden")
public class GenreModel {

	@Id
	@Column(name = "genre_Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int genreId;

	@Column(name = "genre_name")
	private String genreName;
	@Column(name = "genre_desc")
	private String genreDesc;
}
