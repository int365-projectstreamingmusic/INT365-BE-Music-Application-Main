package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "file_link_ref", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class FileLinkRefModel {
	@Id
	@Column(name = "file_id")
	String fileId;
	
	@Column(name = "target_ref")
	private int targetRef;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private FileTypeModel fileType;

}
