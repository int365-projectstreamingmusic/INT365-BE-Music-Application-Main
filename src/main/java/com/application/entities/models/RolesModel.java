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
@Table(name = "roles", schema = "sitgarden")
public class RolesModel {

	@Id
	private int roles_id;
	private String roles;

	@Column(name = "roles_strength")
	private int roleStrength;

}
