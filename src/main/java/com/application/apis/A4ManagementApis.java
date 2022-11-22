package com.application.apis;

import java.net.URI;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.CommentsController;
import com.application.controllers.ReportController;
import com.application.controllers.TrackController;
import com.application.controllers.UserAccountController;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ReportOutput;

@RestController
@RequestMapping("api/manager/")
public class A4ManagementApis {

	private static String mapping = "/api/manager/";

	@Autowired
	private UserAccountController userAccountManagerController;
	@Autowired
	private CommentsController commentsController;
	@Autowired
	private ReportController reportController;
	@Autowired
	private TrackController trackController;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5. OK!
	// listUserByNameOrEmail
	@GetMapping("")
	public ResponseEntity<Page<UserAccountModel>> listUserByNameOrEmail(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size,
			@RequestParam(defaultValue = "", required = false) String searchContent) {
		return ResponseEntity.ok()
				.body(userAccountManagerController.searchUserByNameOrEmail(searchContent, page, size));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	@GetMapping("comment")
	public ResponseEntity<Map<String, Object>> getUserComment(@RequestParam(required = true) int userId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int pageSize) {
		return ResponseEntity.ok().body(commentsController.listUserComment(userId, page, pageSize));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// USER : Assign role to user.
	@PutMapping("role/assign")
	public ResponseEntity<String> assignRoleToUser(@RequestParam(required = true) int userId,
			@RequestParam(required = true) int roleId, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "role/assign").toString());
		return ResponseEntity.created(uri).body(userAccountManagerController.grantRole(userId, roleId, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// USER : Unassign role from user.
	@PutMapping("role/revoke")
	public ResponseEntity<String> revokeRoleToUser(@RequestParam(required = true) int userId,
			@RequestParam(required = true) int roleId, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "role/revoke").toString());
		return ResponseEntity.created(uri).body(userAccountManagerController.revokeRole(userId, roleId, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// User Management
	// ---------------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// USER : Suspend user or unsuspend user.
	@PutMapping("suspend")
	public ResponseEntity<String> suspendUser(@RequestParam(required = true) int userId, HttpServletRequest request) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "suspend").toString());
		return ResponseEntity.created(uri).body(userAccountManagerController.switchSuspendUser(userId, request));
	}

	// COMMENT : Delete comment from specific track.
	@DeleteMapping("comment")
	public ResponseEntity<String> deleteTrackComment(@RequestParam(required = true) int commentId,
			@RequestParam(required = true) int commentType, @RequestParam(defaultValue = "") String reason,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/track").toString());
		return ResponseEntity.created(uri)
				.body(commentsController.deleteComment(commentId, commentType, reason, request));
	}

	// ACTION : Browse all action

	// REPORT : Close report solved.

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Reports
	// ---------------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// REPORT : Get all reports.
	@GetMapping("report")
	public ResponseEntity<Page<ReportGroupModel>> listAllReports(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(defaultValue = "") String searchKey,
			@RequestParam(defaultValue = "0") int reportType) {
		return ResponseEntity.ok().body(reportController.getReportList(page, pageSize, searchKey, reportType));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// REPORT : Get all reports.
	@GetMapping("report/{id}")
	public ResponseEntity<ReportGroupModel> getReportGroupById(@PathVariable int id) {
		return ResponseEntity.ok().body(reportController.getReportListInGroup(id));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// REPORT : Solve Report
	@PutMapping("report/solve")
	public ResponseEntity<String> solveReport(@RequestParam(required = true) int id, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "report/solve").toString());
		return ResponseEntity.created(uri).body("Is solved, now : " + reportController.markResolved(id, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK
	// REPORT : Delete report
	@DeleteMapping("report/{id}")
	public ResponseEntity<String> deleteReport(@PathVariable int id, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "report/" + id).toString());
		reportController.deleteReportGroup(id, request);
		return ResponseEntity.created(uri).body("Report is deleted.");
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Tracks
	// ---------------------
	@DeleteMapping("track")
	public ResponseEntity<String> deleteTrack(@RequestParam(required = true) int trackId,
			@RequestParam(defaultValue = "") String reason, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/playlist").toString());
		return ResponseEntity.created(uri).body(trackController.deleteTrack(trackId, reason, request));
	}

}
