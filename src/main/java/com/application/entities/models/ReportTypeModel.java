package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "report_type", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class ReportTypeModel {
	@Id
	@Column(name = "type_id")
	private int id;

	@Column(name = "type_title")
	private String typeTitle;

	@Column(name = "type_desc")
	private String typeDesc;

}
