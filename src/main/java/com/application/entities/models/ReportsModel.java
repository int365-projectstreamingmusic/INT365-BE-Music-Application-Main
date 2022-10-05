package com.application.entities.models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reports", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class ReportsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "report_id")
	private int id;

	//@Column(name = "reported_by")
	@ManyToOne
	@JoinColumn(name = "reported_by", referencedColumnName = "account_id", insertable = false, updatable = false)
	private UserAccountModel reportedBy;

	//@Column(name = "reported_user")
	@Nullable
	@Basic(optional = true)
	@ManyToOne
	@JoinColumn(name = "reported_user", referencedColumnName = "account_id", insertable = false, updatable = false)
	private UserAccountModel reportedToUser;

	@Column(name = "is_solved")
	private int isSolved;

	@Column(name = "report_date")
	@JsonFormat(pattern = "yyyy/mm/dd")
	private String reportDate;

	@Column(name = "solved_date")
	@JsonFormat(pattern = "yyyy/mm/dd")
	private String solveDate;

	@Column(name = "reference_source")
	private int referenceSource;

	@Column(name = "report_text")
	private String reportText;
	
	@Column(name = "report_ref")
	private String reportRef;
	
	@ManyToOne
	@JoinColumn(name = "type_id", referencedColumnName = "type_id")
	private ReportTypeModel type;

}
