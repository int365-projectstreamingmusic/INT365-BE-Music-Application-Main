package com.application.entities.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_accounts", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int account_id;

	private String username;
	private String email;

	private String first_name;
	private String last_name;
	private String user_bios;

	private String profile_name;

	@JsonFormat(pattern = "yyy/mm/dd")
	private String registered_date;
	@JsonFormat(pattern = "yyy/mm/dd")
	private String last_seen;

	@JsonIgnore
	private String user_passcode;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "account_id")
	private List<UserRolesModel> userRoles;
}
