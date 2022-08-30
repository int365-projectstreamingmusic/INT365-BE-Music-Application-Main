package com.application.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.GenreController;
import com.application.entities.models.GenreModel;

@RestController
@RequestMapping("api/public/genre/")
public class PublicGenreAPIs {

	@Autowired
	private GenreController genreController;

	// ListGenreByPage
	@GetMapping("getGere")
	public ResponseEntity<Page<GenreModel>> listGenreByPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "50") int size,
			@RequestParam(defaultValue = "", required = false) String searchContent) {
		return ResponseEntity.ok().body(genreController.listGenreListByPage(page, size, searchContent));
	}

}
