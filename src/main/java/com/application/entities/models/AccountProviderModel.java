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
@Table(name = "account_provider", schema = "sitgarden")
public class AccountProviderModel {

	@Id
	@Column(name = "id")
	private int id;

	private String provider;
	private String desc;

}