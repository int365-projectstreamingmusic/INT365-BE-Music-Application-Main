package com.application.apis;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.PlayHistoryController;
import com.application.entities.models.PlayHistoryModel;

@RestController
@RequestMapping("/api/user/history")
public class UserHistoryManagementApi {

	private static String mapping = "/api/user/history";

	@Autowired
	private PlayHistoryController playHistoryController;

	@GetMapping("MyHistory")
	public ResponseEntity<Page<PlayHistoryModel>> getMyHistory(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playHistoryController.getMyHistory(page, pageSize, searchContent, request));
	}

	@GetMapping("GetSpecifiedRecord")
	public ResponseEntity<PlayHistoryModel> getSpecifiedRecord(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playHistoryController.getRecordsByUserIdAndTrackId(trackId, request));
	}

	@DeleteMapping("Clear")
	public ResponseEntity<HttpStatus> clearHistory(@RequestParam(defaultValue = "0") int inPassMinutes,
			HttpServletRequest request) {
		if (inPassMinutes == 0) {
			playHistoryController.clearHistory(request);
		} else {
			playHistoryController.clearHistoryInThePassHoures(request, inPassMinutes);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Clear").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	@DeleteMapping("DeleteSpecificHistory")
	public ResponseEntity<HttpStatus> deleteSpecificHistory(@RequestParam(defaultValue = "0") int historyId,
			@RequestParam(defaultValue = "0") int trackId, HttpServletRequest request) {
		if (historyId == 0) {
			playHistoryController.deleteRecordByUserIdAndTrackId(trackId, request);
		} else {
			playHistoryController.deleteRecordById(historyId, request);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(mapping + "DeleteSpecificHistory").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

}
