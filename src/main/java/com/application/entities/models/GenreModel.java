package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "genre", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GenreModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int genre_id;

	private String genre_name;
	private String genre_desc;
}
