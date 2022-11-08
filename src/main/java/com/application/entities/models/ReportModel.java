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

	/*@ManyToOne
	@JoinColumn(name = "report_group_id", referencedColumnName = "id")
	private ReportGroupModel reportGroup;*/
	
	@ManyToOne
	@JoinColumn(name = "reported_by",referencedColumnName = "account_id")
	private UserAccountModel reportedBy;

}
