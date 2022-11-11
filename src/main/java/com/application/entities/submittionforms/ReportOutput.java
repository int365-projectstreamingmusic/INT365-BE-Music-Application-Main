package com.application.entities.submittionforms;

import org.springframework.data.domain.Page;

import com.application.entities.models.ReportGroupModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOutput {

	private int numberOfReport;
	private int hoursAfterFirstReport;
	private String firstReportDate;

	private Page<ReportGroupModel> reportGroup;

}
