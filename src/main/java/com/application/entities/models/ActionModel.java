package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "action_history", schema = "sitgarden")
public class ActionModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "desc")
	private String desc;

	@Column(name = "timestamp")
	private String timestamp;

	@Column(name = "target_ref")
	private int target;

	@Column(name = "account_id")
	private int account;

	@Column(name = "action_type_id")
	private int type;

}
