package com.application.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.copmskeys.GenreTracksCompkey;
import com.application.entities.copmskeys.PlaylistGenreCompKey;
import com.application.entities.models.GenreModel;
import com.application.entities.models.GenresTracksModel;
import com.application.entities.models.GenrePlaylistModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.GenrePlaylistRepository;
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
	@Autowired
	private GenrePlaylistRepository genrePlaylistRepository;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// NOTE | Will add all genres in the track with incoming one.
	// NOTE | Will skip if the genre ID given is wrong.
	public List<GenresTracksModel> addGenreTrack(int trackId, List<GenreModel> genreForm) {
		List<GenresTracksModel> result = new ArrayList<>();
		for (int i = 0; i < genreForm.size(); i++) {
			if (genreRepository.existsById(genreForm.get(i).getGenreId())) {
				genresTracksRepository.insertNewGenreTrack(trackId, genreForm.get(i).getGenreId());
				result.add(new GenresTracksModel(new GenreTracksCompkey(trackId, genreForm.get(i).getGenreId()),
						genreForm.get(i)));
			}
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// NOTE | Will add all genres in the playlist with incoming one.
	// NOTE | Will skip if the genre ID given is wrong.
	public List<GenrePlaylistModel> addGenrePlaylist(int playlistId, List<GenreModel> genreForm) {
		List<GenrePlaylistModel> result = new ArrayList<>();
		for (int i = 0; i < genreForm.size(); i++) {
			if (genreRepository.existsById(genreForm.get(i).getGenreId())) {
				genrePlaylistRepository.insertNewGenrePlaylist(playlistId, genreForm.get(i).getGenreId());
				result.add(new GenrePlaylistModel(new PlaylistGenreCompKey(playlistId, genreForm.get(i).getGenreId()),
						genreForm.get(i)));
			}
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// NOTE | Purge all genres from either track or playlist.
	public void purgeGenreTrack(int trackId) {
		genresTracksRepository.deleteGenreTrack(trackId);
	}

	public void purgeGenrePlaylist(int playlistId) {
		genrePlaylistRepository.deleteGenrePlaylist(playlistId);
	}

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
	// EXCEPTION | 20004 | AUTHEN_NOT_THE_OWNER
	public void addGenreToTrack(int genreId, int trackId, HttpServletRequest request) {

		UserAccountModel requestedBy = userAccountModelRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));

		TracksModel targetTrack = tracksModelRepository.findById(trackId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addGenreToTrack ] Track with this ID does not exist."));

		GenreModel targetGenre = genreRepository.findById(genreId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ addGenreToTrack ] Genre with this ID does not exist."));

		if (requestedBy.getAccountId() != targetTrack.getOwner().getAccountId()) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.UNAUTHORIZED,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not the owner of this track.");
		}

		GenresTracksModel newGenreTrack = new GenresTracksModel();
		newGenreTrack.setId(new GenreTracksCompkey(targetTrack.getId(), targetGenre.getGenreId()));

	}

	// DB-V5.1 OK!
	// Genres : AddGenreToTrack
	public List<GenresTracksModel> addGenreToTrack(int trackId, List<GenreModel> incomingGenre) {
		List<GenreModel> existingGenre = genreRepository.findGenreByTrackId(trackId);

		// This will check if there is a removed genre.
		for (int i = 0; i < existingGenre.size(); i++) {
			GenreTracksCompkey id = new GenreTracksCompkey(trackId, existingGenre.get(i).getGenreId());
			if (!incomingGenre.contains(existingGenre.get(i))) {
			} else {
				genresTracksRepository.deleteById(id);
			}
		}

		// This will add a genre that does not curently exist.
		for (int i = 0; i < incomingGenre.size(); i++) {
			GenreTracksCompkey id = new GenreTracksCompkey(trackId, incomingGenre.get(i).getGenreId());
			if (!genresTracksRepository.existsById(id)) {
				genresTracksRepository.insertNewGenreTrack(id.getTrackId(), id.getGenreId());
				existingGenre.add(incomingGenre.get(i));
			}
		}
		return genresTracksRepository.findAllByTrackId(trackId);
	}

	// RemoveGenreFromTrack
	public void removeGenreFromTrack(int genreId, int trackId, HttpServletRequest request) {

		GenresTracksModel target = genresTracksRepository.findById(new GenreTracksCompkey(trackId, genreId))
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, HttpStatus.NOT_FOUND,
						"[ GenreController ] The target with track id " + trackId + " and genre id " + genreId
								+ " does not exist."));

		genresTracksRepository.delete(target);

	}

	// Add genre into playlist
	public List<GenrePlaylistModel> addPlaylistGenre(int playlistId, List<GenreModel> genreForm) {
		List<GenreModel> existingGenre = genreRepository.fineGenreByPlaylistId(playlistId);
		List<GenrePlaylistModel> genreList = new ArrayList<>();
		for (int i = 0; i < genreForm.size(); i++) {
			if (!existingGenre.contains(genreForm.get(i))) {
				GenrePlaylistModel playlistGenre = new GenrePlaylistModel();
				playlistGenre.setId(new PlaylistGenreCompKey(playlistId, genreForm.get(i).getGenreId()));
				genreList.add(playlistGenre);
				try {
					genrePlaylistRepository.save(playlistGenre);
				} catch (Exception exc) {
					System.out.println("[ WARN ] Genre ID is invlid.");
				}
			}

		}
		return genreList;
	}

}
