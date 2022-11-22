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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_types", schema = "sitgarden")
public class FileTypeModel {

	@Id
	@Column(name = "type_id")
	int typeId;

	@Column(name = "type_desc")
	String typeDesc;

	@Column(name = "path_rel")
	String pathRel;
}
