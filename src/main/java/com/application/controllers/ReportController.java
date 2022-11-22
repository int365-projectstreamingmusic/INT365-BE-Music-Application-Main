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
import com.application.entities.models.ReportGenreModel;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.ReportModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ActionForm;
import com.application.entities.submittionforms.ReportForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.CommentPlaylistRepository;
import com.application.repositories.CommentTrackRepository;
import com.application.repositories.ReportGenreRepository;
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
	private ReportGenreRepository reportGenreRepository;

	@Autowired
	private ActionHistoryController actionHistoryController;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	private int defaultPageSize = 100;
	private int defauleMaxPageSize = 250;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Get report genre
	public List<ReportGenreModel> getReportGenre(int typeId) {
		return reportGenreRepository.listGenres(typeId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Get report list.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Page<ReportGroupModel> getReportList(int page, int pageSize, String searchKey, int reportType,
			boolean isSolved) {
		Page<ReportGroupModel> result;
		result = reportGroupRepository.listReportGroup(getPageRequest(page, pageSize), searchKey, reportType, isSolved);
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] There is no report of this search or type that needs to be handled for a moment..");
		}
		List<ReportGroupModel> reportlLst = new ArrayList<>();
		for (int i = 0; i < result.getContent().size(); i++) {
			ReportGroupModel current = result.getContent().get(i);
			current.setNumberOfReport(result.getContent().get(i).getReports().size());
			// current.setReports(null);

			switch (current.getType().getId()) {
			case 1001: {
				TracksModel track = tracksRepository.findById(current.getTarget()).orElse(null);
				current.setTrack(track);
				current.setNote(current.getTrack() == null ? "This track is no longer exist. " : "");
				current.setReportedReason(
						reportGenreRepository.listGenreReported(reportType, track.getId()).toString());
				if (track != null) {
					reportlLst.add(current);
				}
				break;
			}
			case 2001: {
				current.setCommentTrack(commentTrackRepository.findById(current.getTarget()).orElse(null));
				current.setNote(current.getCommentTrack() == null ? "This comment is no longer exist. " : "");
				current.setReportedReason(reportGenreRepository
						.listGenreReported(reportType, current.getCommentTrack().getId()).toString());
				if (current.getCommentTrack() != null || current.getCommentPlaylist() != null) {
					reportlLst.add(current);
				}
				break;
			}
			case 2002: {
				current.setCommentPlaylist(commentPlaylistRepository.findById(current.getTarget()).orElse(null));
				current.setNote(current.getCommentPlaylist() == null ? "This comment is no longer exist. " : "");
				current.setReportedReason(reportGenreRepository
						.listGenreReported(reportType, current.getCommentPlaylist().getId()).toString());
				if (current.getCommentTrack() != null || current.getCommentPlaylist() != null) {
					reportlLst.add(current);
				}
				break;
			}
			}

		}
		return new PageImpl<>(reportlLst, getPageRequest(page, pageSize), result.getTotalElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Get report list made by a specific user, most likely be used by the user.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Page<ReportGroupModel> getOwnedReport(int page, int pageSize, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		Page<ReportGroupModel> result;
		result = reportGroupRepository.listReportGroupByOwner(getPageRequest(page, pageSize), owner.getAccountId());
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] This user has no report made against anything.");
		}
		List<ReportGroupModel> reportlLst = new ArrayList<>();
		for (int i = 0; i < result.getContent().size(); i++) {
			ReportGroupModel current = result.getContent().get(i);
			current.setNumberOfReport(result.getContent().get(i).getReports().size());
			current.setReports(null);
			reportlLst.add(current);
		}
		return new PageImpl<>(reportlLst, getPageRequest(page, pageSize), result.getTotalElements());
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Get pageRequest
	private Pageable getPageRequest(int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize > defauleMaxPageSize || pageSize <= 1) {
			pageSize = defaultPageSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		return sendPageRequest;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Get report in specific group.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public ReportGroupModel getReportListInGroup(int reportGroupId) {
		ReportGroupModel reportGroup = reportGroupRepository.findById(reportGroupId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The report group you are looking for does not exist."));
		reportGroup.setReports(reportsRepository.getReportList(reportGroupId));
		return reportGroup;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Delete report by Id, ( Only be able to delete a report group. )
	// NOTE | Keeps history when deleting a report.
	// CONDITION | Only the staff can delete.
	// CONDITION | If the report isn't resolved.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	// EXCEPTION | 80003 | REPORT_INVALID_STATUS
	public void deleteReportGroup(int groupId, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		ReportGroupModel reportGroup = reportGroupRepository.findById(groupId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The report does not exist, or is already deleted."));

		if (reportGroup.isSolved()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REPORT_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
					"[ REPORT_INVALID_STATUS ] You are not allowed to delete solved report.");
		} else {
			actionHistoryController.addNewRecord(new ActionForm(staff, reportGroup.getId(), 503,
					"User, " + staff.getUsername() + " deleted the report group ID " + reportGroup.getId() + "."));
			reportGroupRepository.deleteRecordById(groupId);
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// Mark as resolved.
	// NOTE | Keeps history when resolve status is changed.
	// NOTE | Once resolved, there will be no turning back.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public boolean markResolved(int reportGroupId, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		ReportGroupModel reportGroup = reportGroupRepository.findById(reportGroupId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ]"));
		if (reportGroup.isSolved()) {
			reportGroupRepository.updateIsSolved(reportGroupId, false);
			// Save history
			actionHistoryController.addNewRecord(new ActionForm(staff, reportGroup.getId(), 502,
					"Staff or admin ID " + staff.getAccountId() + " or known as \"" + staff.getUsername()
							+ "\"removed solved status from report group ID " + reportGroup.getId() + "."));
			return false;
		} else {
			reportGroupRepository.updateIsSolved(reportGroupId, true);
			// Save history
			actionHistoryController.addNewRecord(new ActionForm(staff, reportGroup.getId(), 501,
					"Staff or admin ID " + staff.getAccountId() + " or known as \"" + staff.getUsername()
							+ "\" marked report group id [ " + reportGroup.getId() + " ] as solved."));
			return true;
		}
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V.6 OK!
	// The report is made by the user
	// ---
	// Needs | reportRef, reportMsg, reportType
	// Optional | reportGroupId, reportTitle
	// EXCEPTION | 80001 | REPORT_ALREADY_MADE
	// EXCEPTION | 80002 | REPORT_INVALID_TYPE
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	// EXCPETION | 40008 | REPORT_INVALID_GENRE
	public ReportModel createNewReport(ReportForm form, HttpServletRequest request) {
		UserAccountModel userMakingReport = generalFunctionController.getUserAccount(request);
		String currentdate = new Timestamp(Calendar.getInstance().getTimeInMillis()).toString();

		// If the user already made the report to an unsolved target, reject them.
		if (reportsRepository.existByReport(form.getReportType(), form.getTargetRef(),
				userMakingReport.getAccountId()) == 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.REPORT_ALREADY_MADE, HttpStatus.I_AM_A_TEAPOT,
					"[ REPORT_ALREADY_MADE ] You've already made a report for this target.");
		}

		ReportGenreModel genre;
		if (form.getReportGenreId() != 0) {
			genre = reportGenreRepository.findById(form.getReportGenreId())
					.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.REPORT_INVALID_GENRE,
							HttpStatus.I_AM_A_TEAPOT, "[ REPORT_INVALID_GENRE ] Invalid report genre ID."));
		} else {
			genre = reportGenreRepository.findById(10).orElseThrow(() -> new ExceptionFoundation(
					EXCEPTION_CODES.REPORT_INVALID_GENRE, HttpStatus.I_AM_A_TEAPOT,
					"[ REPORT_INVALID_GENRE ] The database records are not initialized correctly, please see the database creation insert again."));
		}

		ReportModel newReport = new ReportModel();
		newReport.setReportGenre(genre);
		newReport.setReportedDate(currentdate);
		newReport.setReportText(form.getReportMsg());
		newReport.setReportGroupId(form.getReportGroupId());
		newReport.setReportedById(userMakingReport.getAccountId());
		newReport.setReportedBy(userMakingReport);

		ReportGroupModel reportGroup = reportGroupRepository.getByRefIdAndType(form.getReportType(),
				form.getTargetRef());

		if (reportGroup == null) {
			ReportGroupModel newReportGroup = new ReportGroupModel();
			newReportGroup.setTarget(form.getTargetRef());
			newReportGroup.setStartedBy(userMakingReport.getAccountId());
			newReportGroup.setType(reportTypeRepository.findById(form.getReportType())
					.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.REPORT_INVALID_TYPE,
							HttpStatus.NOT_FOUND,
							"[ REPORT_INVALID_TYPE ] Invalid Report Type ID. If you think this is not right, please check our database document for those available status. ")));

			switch (form.getReportType()) {
			case 1001: {
				newReportGroup.setTitle(getReportedTrack(form, userMakingReport));
				break;
			}
			case 2001: {
				newReportGroup.setTitle(getReportedTrackComment(form, userMakingReport));
				break;
			}
			case 2002: {
				newReportGroup.setTitle(getReportedPlaylistComment(form, userMakingReport));
				break;
			}
			}
			newReportGroup.setRecentDate(currentdate);
			newReportGroup.setSolved(false);
			newReportGroup.setSolvedDate(null);
			newReportGroup = reportGroupRepository.save(newReportGroup);
			newReport.setReportGroupId(newReportGroup.getId());
			newReport = reportsRepository.save(newReport);
		} else {
			newReport.setReportGroupId(reportGroup.getId());
			newReport = reportsRepository.save(newReport);
		}
		return newReport;
	}

	// Check if track is exist in the database.
	private String getReportedTrack(ReportForm form, UserAccountModel user) {
		TracksModel target = tracksRepository.findById(form.getTargetRef())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The track with this ID does not exist."));
		String message = "";
		if (form.getReportTitle() == "") {
			message = "Track Report : Track name " + target.getTrackName() + " is reported by " + user.getUsername();
		} else {
			message = form.getReportTitle();
		}
		return message;
	}

	// Check if track comment is exist in the database.
	private String getReportedTrackComment(ReportForm form, UserAccountModel user) {
		CommentTrackModel target = commentTrackRepository.findById(form.getTargetRef()).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The track comment with this ID does not exist."));

		String message = "";
		if (form.getReportTitle() == "") {
			message = "User Track Comment Report : User " + user.getUsername() + " reported "
					+ target.getUser().getUsername() + "'s comment.";
		} else {
			message = form.getReportTitle();
		}
		return message;
	}

	// Check if playlist comment is exist in the database.
	private String getReportedPlaylistComment(ReportForm form, UserAccountModel user) {
		CommentPlaylistModel target = commentPlaylistRepository.findById(form.getTargetRef()).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The playlist comment with this ID does not exist."));

		String message = "";
		if (form.getReportTitle() == "") {
			message = "User Playlist Comment Report : User " + user.getUsername() + " reported "
					+ target.getUser().getUsername() + "'s comment.";
		} else {
			message = form.getReportTitle();
		}
		return message;
	}
}
