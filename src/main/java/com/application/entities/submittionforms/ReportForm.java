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
	private int id;
	@Basic(optional = true)
	private int reportRef;

	@Basic(optional = true)
	private int reportType;
	@Basic(optional = true)
	private String reportMsg;
	@Basic(optional = true)
	private int reportGroupId;

}
