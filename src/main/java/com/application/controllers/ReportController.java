package com.application.controllers;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.ReportTypeModel;
import com.application.entities.models.ReportsModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ReportForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.ReportGroupRepository;
import com.application.repositories.ReportTypeRepository;
import com.application.repositories.ReportsRepository;
import com.application.services.GeneralFunctionController;

@Service
public class ReportController {

	@Autowired
	private ReportsRepository reportsRepository;
	@Autowired
	private ReportGroupRepository reportGroupRepository;
	@Autowired
	private ReportTypeRepository reportTypeRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// send report by user
	public ReportsModel createNewReport(ReportForm form, HttpServletRequest request) {
		ReportsModel report = new ReportsModel();

		ReportTypeModel reportType = reportTypeRepository.findById(form.getReportType())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] Invalid report type."));

		report.setReferenceSource(0);
		report.setReportDate(new Timestamp(System.currentTimeMillis()).toString());
		report.setReportedBy(null);
		report.setReportedToUser(null);
		report.setReportRef(form.getReportRef());
		report.setReportText(form.getReportMsg());

		report.setType(reportType);
		report.setSolved(false);
		report.setSolveDate(null);

		if (form.getReportGroupId() <= 0) {
			ReportGroupModel group = new ReportGroupModel();
			group.setGroupName(null);
			reportGroupRepository.save(group);
			report.setReportGroup(group);
		}

		reportsRepository.save(report);
		return report;
	}

	// Solve report by admin
	public void solverReport(int reportId, String message, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		ReportsModel report = reportsRepository.findById(reportId).orElseThrow(() -> new ExceptionFoundation(
				EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
				"[ BROWSE_NO_RECORD_EXISTS ] The report id " + reportId + " does not exist or deleted by user."));
		report.setSolved(true);
		report.setSolveDate(new Timestamp(System.currentTimeMillis()).toString());
		reportsRepository.save(report);
	}

	// cancle report by user if it is not solved.
	public void cancleReport(int reportId, HttpServletRequest request) {
		
	}

}
