package com.application.entities.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	@Column(name = "account_id")
	private int accountId;

	private String username;
	private String email;

	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	@Column(name = "user_bios")
	private String userBios;

	@JsonFormat(pattern = "yyyy/mm/dd")
	private String registered_date;
	@JsonFormat(pattern = "yyyy/mm/dd")
	private String last_seen;

	@JsonIgnore
	@Column(name = "user_passcode")
	private String userPasscode;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "account_id")
	private List<UserRolesModel> userRoles;
	
}
