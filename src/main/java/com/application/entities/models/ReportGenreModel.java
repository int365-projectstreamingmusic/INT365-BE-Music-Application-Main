package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report_genre", schema = "sitgarden")
public class ReportGenreModel {

	@Id
	@Column(name = "id")
	private int genreId;

	@Column(name = "report_genre")
	private String genre;

	@Column(name = "report_description")
	private String description;

	@Transient
	private int genreCount;

}
