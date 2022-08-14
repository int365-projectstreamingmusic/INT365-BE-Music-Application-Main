package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class RolesModel {
	@Id
	private int roles_id;
	private String roles;
}
