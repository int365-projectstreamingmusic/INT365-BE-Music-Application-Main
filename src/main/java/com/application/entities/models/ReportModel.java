package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reports", schema = "sitgarden")
public class ReportModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private int id;

	@Column(name = "report_date")
	private String reportedDate;

	@Column(name = "report_text")
	private String reportText;

	@Column(name = "report_group_id")
	private int reportGroupId;

	@Column(name = "reported_by")
	private int reportedById;

	@Transient
	private UserAccountModel reportedBy;

}
