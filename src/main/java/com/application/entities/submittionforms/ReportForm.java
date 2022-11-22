package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportForm {

	@Basic(optional = true)
	private int reportId;

	// Related to report.
	@Basic(optional = true)
	private String reportMsg;
	@Basic(optional = true)
	private int reportGroupId;
	@Basic(optional = true)
	private int reportGenreId;

	// Related to report group.
	@Basic(optional = true)
	private String reportTitle;
	@Basic(optional = true)
	private int reportType;
	@Basic(optional = true)
	private int targetRef;

}
