package com.application.controllers;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.ReportModel;
import com.application.entities.models.ReportTypeModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ReportForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.CommentPlaylistRepository;
import com.application.repositories.CommentTrackRepository;
import com.application.repositories.ReportGroupRepository;
import com.application.repositories.ReportTypeRepository;
import com.application.repositories.ReportsRepository;
import com.application.repositories.TracksRepository;
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
	private TracksRepository tracksRepository;
	@Autowired
	private CommentPlaylistRepository commentPlaylistRepository;
	@Autowired
	private CommentTrackRepository commentTrackRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// The report is made by the user
	// ---
	// Needs | reportRef, reportMsg, reportType
	// Optional | reportGroupId, reportTitle
	public ReportModel createReport(ReportForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		String currentdate = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();

		ReportModel newReport = new ReportModel();
		newReport.setReportedBy(user);
		newReport.setReportedDate(currentdate);
		newReport.setReportText(form.getReportMsg());

		if (true) {
			ReportGroupModel newGroup = new ReportGroupModel();
			newGroup.setIsSolved(0);
			newGroup.setTarget(form.getReportRef());

			switch (form.getReportType()) {
			case 1001: {
				TracksModel target = tracksRepository.findById(form.getReportRef())
						.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
								HttpStatus.NOT_FOUND,
								"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist."));
				newGroup.setTitle(
						"Track Report : Track name " + target.getTrackName() + " is reported by " + user.getUsername());
			}
			case 2001: {
				CommentPlaylistModel target = commentPlaylistRepository.findById(form.getReportRef())
						.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
								HttpStatus.NOT_FOUND,
								"[ BROWSE_NO_RECORD_EXISTS ] The playlist comment with this ID does not exist."));
				newGroup.setTitle("User Playlist Comment Report : User " + user.getUsername() + " reported "
						+ target.getUser().getUsername() + "'s comment.");
			}
			case 2002: {
				CommentTrackModel target = commentTrackRepository.findById(form.getReportRef())
						.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
								HttpStatus.NOT_FOUND,
								"[ BROWSE_NO_RECORD_EXISTS ] The track comment with this ID does not exist."));
				newGroup.setTitle("User Playlist Comment Report : User " + user.getUsername() + " reported "
						+ target.getUser().getUsername() + "'s comment.");

			}
			case 3001: {
				newGroup.setTitle("But reported by " + user.getUsername() + " | ");
				newGroup.setTarget(0);
			}

			}
			newGroup.setType(reportTypeRepository.findById(form.getReportType())
					.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
							"[ SEARCH_NOT_FOUND ] Invalid Status ID. Please check our database document for those available status. ")));
			newGroup.setSolvedDate(null);
			newGroup.setIsSolved(0);
		}
		return null;
	}
}
