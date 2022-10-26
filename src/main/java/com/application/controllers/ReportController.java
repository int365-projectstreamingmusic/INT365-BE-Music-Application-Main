package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.ReportsModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ReportForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ReportsRepository;
import com.application.services.GeneralFunctionController;

@Service
public class ReportController {

	@Autowired
	private ReportsRepository reportsRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// send report by user
	public ReportsModel createNewReport(ReportForm form, HttpServletRequest request) {
		ReportsModel report = new ReportsModel();
		report.setSolved(false);
		report.setReferenceSource(0);
		report.setReportDate(null);
		report.setReportedBy(null);
		report.setReportedToUser(null);
		report.setReportRef(null);
		report.setReportText(null);
		report.setSolveDate(null);
		report.setType(null);
		return report;
	}

	// Solve report by admin
	public void solverReport(int reportId, String message, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		ReportsModel report = reportsRepository.findById(reportId).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
				"[ BROWSE_NO_RECORD_EXISTS ] The report id " + reportId + " does not exist or deleted by user."));
		report.setSolved(true);
		reportsRepository.save(report);
	}

	// Reply to report

	// cancle report by user if it is not solved.

}
