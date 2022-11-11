package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.ReportModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ActionForm;
import com.application.entities.submittionforms.ReportForm;
import com.application.entities.submittionforms.ReportOutput;
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
	private ActionHistoryController actionHistoryController;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	private int defaultPageSize = 100;
	private int defauleMaxPageSize = 250;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get report detail.
	public ReportOutput getReportStatistic(int page, int pageSize, int targetRef, int reportType) {
		Page<ReportGroupModel> reportGroupList = reportGroupRepository.listReportGroup(getPageRequest(page, pageSize),
				reportType, targetRef);
		if (reportGroupList.getNumberOfElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] The report group you're looking for does not exist.");
		}

		// Assign report list into each report group.
		List<ReportGroupModel> newReportGroupList = new ArrayList<>();
		for (int i = 0; i <= reportGroupList.getContent().size(); i++) {
			ReportGroupModel currentGroup = reportGroupList.getContent().get(i);
			List<ReportModel> reportList = reportsRepository.getReportList(reportGroupList.getContent().get(i).getId());
			currentGroup.setReports(reportList);
			newReportGroupList.add(currentGroup);
		}

		// Ready to user.
		Page<ReportGroupModel> newListResult = new PageImpl<>(newReportGroupList, getPageRequest(page, pageSize),
				reportType);
		ReportGroupModel firstReportGroup = newListResult.getContent().get(newListResult.getContent().size());
		ReportModel firstReport = firstReportGroup.getReports().get(firstReportGroup.getReports().size());
		long currentDateMilisecond = Calendar.getInstance().getTimeInMillis();
		long firstReportDateMinisecond = currentDateMilisecond
				- generalFunctionController.getTimeStampFromString(firstReport.getReportedDate());

		// Create a result output.
		ReportOutput result = new ReportOutput();
		result.setNumberOfReport(reportGroupList.getContent().size());
		result.setHoursAfterFirstReport((int) firstReportDateMinisecond / (1000 * 60 * 60));
		result.setFirstReportDate(firstReport.getReportedDate());
		result.setReportGroup(newListResult);
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get report list. Will be listed in group.
	public Page<ReportGroupModel> getReportGroupList(int page, int pageSize, String searchKey) {
		Page<ReportGroupModel> result;
		if (searchKey == "") {
			result = reportGroupRepository.listReportGroup(getPageRequest(page, pageSize));
		} else {
			result = reportGroupRepository.listReportGroup(getPageRequest(page, pageSize), searchKey);
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get report in specific group.
	public ReportGroupModel getReportListInGroup(int reportGroupId) {
		ReportGroupModel reportGroup = reportGroupRepository.findById(reportGroupId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The report group you are looking for does not exist."));
		reportGroup.setReports(reportsRepository.getReportList(reportGroupId));
		return reportGroup;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Delete report by Id, ( Only be able to delete a report group. )
	// CONDITION | Only the owner can delete.
	// CONDITION | If it isn't replied, user can delete.
	// CONDITION | If the report isn't resolved.
	// NOTE | Keeps history when deleting a report.
	public void deleteReportGroup(int groupId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		ReportGroupModel reportGroup = reportGroupRepository.findById(groupId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The report does not exist, or is already deleted."));

		reportGroup.setReports(reportsRepository.getReportList(groupId));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(),
				reportGroup.getReports().get(0).getReportedBy().getAccountId());

		if (reportGroup.getReports().size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] Found report group but no record found. This group is empty.");
		} else if (reportGroup.getReports().size() > 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_HAS_REPLY, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_HAS_REPLY ] You can't delete a report with a reply.");
		} else if (reportGroup.isSolved()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_INVALID_STATUS ] You are not allowed to delete solved report.");
		} else {
			// Save history
			actionHistoryController.addNewRecord(new ActionForm(owner, reportGroup.getId(), 503,
					"User, " + owner.getUsername() + " deleted the report group ID " + reportGroup.getId() + "."));
			reportGroupRepository.deleteById(groupId);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Mark as resolved.
	// NOTE | Keeps history when resolve status is changed.
	public void markResolved(int reportGroupId, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		ReportGroupModel reportGroup = reportGroupRepository.findById(reportGroupId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ]"));
		if (reportGroup.isSolved()) {
			reportGroupRepository.updateIsSolved(false);
			// Save history
			actionHistoryController.addNewRecord(new ActionForm(staff, reportGroup.getId(), 502,
					"Staff or admin ID " + staff.getAccountId() + " or known as \"" + staff.getUsername()
							+ "\"removed solved status from report group ID " + reportGroup.getId() + "."));
		} else {
			reportGroupRepository.updateIsSolved(true);
			// Save history
			actionHistoryController.addNewRecord(new ActionForm(staff, reportGroup.getId(), 501,
					"Staff or admin ID " + staff.getAccountId() + " or known as \"" + staff.getUsername()
							+ "\" marked report group id [ " + reportGroup.getId() + " ] as solved."));
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// The report is made by the user
	// ---
	// Needs | reportRef, reportMsg, reportType
	// Optional | reportGroupId, reportTitle
	public void createReport(ReportForm form, HttpServletRequest request) {
		UserAccountModel user = generalFunctionController.getUserAccount(request);
		String currentdate = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();

		ReportModel newReport = new ReportModel();
		newReport.setReportedBy(user);
		newReport.setReportedDate(currentdate);
		newReport.setReportText(form.getReportMsg());

		if (form.getReportGroupId() != 0) {
			ReportGroupModel newGroup = new ReportGroupModel();
			newGroup.setTarget(form.getReportRef());

			switch (form.getReportType()) {
			case 1001: {
				TracksModel target = tracksRepository.findById(form.getReportRef())
						.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
								HttpStatus.NOT_FOUND,
								"[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist."));
				if (form.getReportTitle() == "") {
					newGroup.setTitle("Track Report : Track name " + target.getTrackName() + " is reported by "
							+ user.getUsername());
				} else {
					newGroup.setTitle(form.getReportTitle());
				}
			}
			case 2001: {
				CommentPlaylistModel target = commentPlaylistRepository.findById(form.getReportRef())
						.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
								HttpStatus.NOT_FOUND,
								"[ BROWSE_NO_RECORD_EXISTS ] The playlist comment with this ID does not exist."));
				if (form.getReportTitle() == "") {
					newGroup.setTitle("User Playlist Comment Report : User " + user.getUsername() + " reported "
							+ target.getUser().getUsername() + "'s comment.");
				} else {
					newGroup.setTitle(form.getReportTitle());
				}
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

			newGroup.setRecentDate(currentdate);
			// This report is newly created, and will be waiting for someone to answer.
			newGroup.setSolvedDate(null);
			newGroup.setSolved(false);

			reportGroupRepository.save(newGroup);
		} else {
			ReportGroupModel currentGroup = reportGroupRepository.findById(form.getReportGroupId()).orElseThrow(
					() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
							"[ BROWSE_NO_RECORD_EXISTS ] The report group with this ID does not exist."));
			newReport.setReportGroupId(currentGroup.getId());
		}

		reportsRepository.save(newReport);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get pagerequest
	private Pageable getPageRequest(int page, int pageSize) {
		if (page < 0) {
			page = defaultPageSize;
		}
		if (pageSize > defauleMaxPageSize) {
			pageSize = defauleMaxPageSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		return sendPageRequest;
	}
}
