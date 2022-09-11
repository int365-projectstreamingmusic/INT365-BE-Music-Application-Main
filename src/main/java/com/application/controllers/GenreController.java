package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.models.GenreModel;
import com.application.entities.models.GenresTracksModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.GenreRepository;
import com.application.repositories.GenresTracksRepository;
import com.application.repositories.TracksRepository;
import com.application.repositories.UserAccountRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class GenreController {

	public static int defaultGenrePerPage = 50;
	public static int maxGenrePerPage = 250;

	@Autowired
	private UserAccountRepository userAccountModelRepository;
	@Autowired
	private TracksRepository tracksModelRepository;
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private GenresTracksRepository genresTracksRepository;

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

	// AddGenreToTrack
	public void addGenreToTrack(int genreId, int trackId, HttpServletRequest request) {

		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		TracksModel targetTrack = tracksModelRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addGenreToTrack ] Track with this ID does not exist."));

		GenreModel targetGenre = genreRepository.findById(genreId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addGenreToTrack ] Genre with this ID does not exist."));

		if (requestedBy.getAccountId() != targetTrack.getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_ALLOWED, HttpStatus.UNAUTHORIZED,
					"[ addGenreToTrack ] This user is not the owner of this track.");
		}

		GenresTracksModel newGenreTrack = new GenresTracksModel();
		newGenreTrack.setId(new GenreTracksCompkey(targetTrack.getId(), targetGenre.getGenreId()));

	}

	// RemoveGenreFromTrack
	public void removeGenreFromTrack(int genreId, int trackId, HttpServletRequest request) {

		GenresTracksModel target = genresTracksRepository.findById(new GenreTracksCompkey(trackId, genreId))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ GenreController ] The target with track id " + trackId + " and genre id " + genreId
								+ " does not exist."));

		genresTracksRepository.delete(target);

	}

}
