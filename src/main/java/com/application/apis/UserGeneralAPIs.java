package com.application.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/")
public class UserGeneralAPIs {

	@GetMapping("test")
	public ResponseEntity<String> testShit() {
		return ResponseEntity.ok().body("HI!");
	}
}
