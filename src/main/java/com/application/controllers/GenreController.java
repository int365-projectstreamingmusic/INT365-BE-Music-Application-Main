package com.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.GenreModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.GenreRepository;

@Service
public class GenreController {

	public static int defaultGenrePerPage = 50;
	public static int maxGenrePerPage = 250;

	@Autowired
	private GenreRepository genreRepository;

	// ListGenreListByPage
	public Page<GenreModel> listGenreListByPage(int page, int size, String searchContent) {
		if (page < 0) {
			page = 0;
		}
		if (size < 1 || size > maxGenrePerPage) {
			size = defaultGenrePerPage;
		}
		if (searchContent == null) {
			searchContent = "";
		}
		Pageable pageRequest = PageRequest.of(page, size);
		Page<GenreModel> result;

		result = genreRepository.findByGenreName(searchContent, pageRequest);
		if (result.getContent().size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ listGenreListByPage ] Seems like nothing found in the genre list.");
		}
		return result;
	}

	// AddNewGenre
	public void addNewGenre(GenreModel addNewGenre) {
		if (genreRepository.existsByGenreName(addNewGenre.getGenreName())) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_EXISTS, HttpStatus.I_AM_A_TEAPOT,
					"[ addNewGenre ] This genre name is already exist.");
		} else if (addNewGenre.getGenreName() == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_IS_NULL, HttpStatus.I_AM_A_TEAPOT,
					"[ addNewGenre ] Your genre nams is null.");
		}

		if (addNewGenre.getGenreDesc() == null) {
			addNewGenre.setGenreDesc("");
		}

		GenreModel newGenre = new GenreModel();
		newGenre.setGenreName(addNewGenre.getGenreName());
		newGenre.setGenreDesc(addNewGenre.getGenreDesc());

		genreRepository.save(newGenre);
	}
	
	

}
