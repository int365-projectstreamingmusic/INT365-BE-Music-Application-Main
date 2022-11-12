package com.application.apis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.CommentsController;
import com.application.controllers.ReportController;
import com.application.controllers.UserAccountController;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.UserAccountModel;

@RestController
@RequestMapping("api/manager/")
public class A4ManagementApis {

	@Autowired
	private UserAccountController userAccountManagerController;
	@Autowired
	private CommentsController commentsController;
	@Autowired
	private ReportController reportController;

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

	// USER : Suspend user or unsuspend user.

	// USER : Assign role to user.

	// USER : Unassign role from user.

	// COMMENT : Delete comment from specific playlist.

	// COMMENT : Delete comment from specific track.

	// ACTION : Browse all action

	// ACTION : Browse all action by staff name.

	// REPORT : Reply to report

	// REPORT : Close report solved.

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Reports
	// ---------------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// REPORT : Get all reports.
	@GetMapping("report")
	public ResponseEntity<Page<ReportGroupModel>> listAllReports(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int pageSize, @RequestParam(defaultValue = "") String searchKey) {
		return ResponseEntity.ok().body(reportController.getReportGroupList(page, pageSize, searchKey));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// REPORT : Get all reports.
	@GetMapping("report/{id}")
	public ResponseEntity<ReportGroupModel> getReportGroupById(@PathVariable int id) {
		return ResponseEntity.ok().body(reportController.getReportListInGroup(id));
	}

}
