package com.application.entities.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.entities.copmskeys.UserRolesCompKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_roles", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesModel {

	@JsonIgnore
	@EmbeddedId
	private UserRolesCompKey id;
	
	@ManyToOne
	@JoinColumn(name = "roles_id", insertable = false, updatable = false)
	private RolesModel roles;
	
}
