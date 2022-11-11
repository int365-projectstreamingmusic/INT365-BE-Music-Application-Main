package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "action_history", schema = "sitgarden")
public class ActionHistoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "description")
	private String description;

	@Column(name = "timestamp")
	private String timestamp;

	@Column(name = "target_ref")
	private int target;

	@Column(name = "account_id")
	private int account;

	@ManyToOne
	@JoinColumn(name = "action_type_id", referencedColumnName = "id")
	private ActionTypeModel type;

}
