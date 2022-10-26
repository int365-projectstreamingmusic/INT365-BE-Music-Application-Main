package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.application.entities.models.ReportsModel;
import com.application.entities.submittionforms.ReportForm;

@Service
public class ReportController {

	// send report by user
	public ReportsModel createNewReport(ReportForm form, HttpServletRequest request) {
		ReportsModel report = new ReportsModel();
		report.setIsSolved(0);
		report.setReferenceSource(0);
		report.setReportDate(null);
		report.setReportedBy(null);
		report.setReportedToUser(null);
		report.setReportRef(null);
		report.setReportText(null);
		report.setSolveDate(null);
		report.setType(null);
		return null;
	}

	// Solve report by admin


	// Reply to report

	// cancle report by user if it is not solved.

}
