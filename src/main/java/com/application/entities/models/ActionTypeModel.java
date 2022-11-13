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
@Table(name = "action_type", schema = "sitgarden")
public class ActionTypeModel {

	@Id
	@Column(name = "id")
	private int id;

	@Column(name = "type")
	private String type;

	@Column(name = "desc")
	private String desc;

}
