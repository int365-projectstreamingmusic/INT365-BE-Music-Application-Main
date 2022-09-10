package com.application.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.UserAccountManagerController;
import com.application.entities.models.UserAccountModel;

@RestController
@RequestMapping("api/manager/user/")
public class ManagerUserManagementApis {

	@Autowired
	private UserAccountManagerController userAccountManagerController;

	// OK!
	// listUserByNameOrEmail
	@GetMapping("")
	public ResponseEntity<Page<UserAccountModel>> listUserByNameOrEmail(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size,
			@RequestParam(defaultValue = "", required = false) String searchContent) {
		return ResponseEntity.ok()
				.body(userAccountManagerController.searchUserByNameOrEmail(searchContent, page, size));
	}
}
