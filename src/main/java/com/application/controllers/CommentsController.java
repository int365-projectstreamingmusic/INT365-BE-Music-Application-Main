package com.application.controllers;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.CommentForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.CommentPlaylistRepository;
import com.application.repositories.CommentTrackRepository;
import com.application.repositories.PlaylistRepository;
import com.application.repositories.TracksRepository;
import com.application.services.GeneralFunctionController;

@Service
public class CommentsController {

	@Autowired
	private CommentTrackRepository commentTrackRepository;
	@Autowired
	private CommentPlaylistRepository commentPlaylistRepository;
	@Autowired
	private PlaylistRepository playlistRepository;
	@Autowired
	private TracksRepository tracksRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;

	private int commentDefaultSize = 100;
	private int commentMaxPageSize = 500;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List only my comment.
	public Map<String, Object> listMyComment(int page, int pageSize, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		return listUserComment(owner.getAccountId(), page, pageSize);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List comments of the user.
	public Map<String, Object> listUserComment(int userId, int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > commentMaxPageSize) {
			pageSize = commentDefaultSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Map<String, Object> result = new HashMap<>();
		result.put("commentTrack", commentTrackRepository.listAllCommentsByUser(userId, sendPageRequest));
		result.put("commmentPlaylist", commentPlaylistRepository.listAllCommentsByUser(userId, sendPageRequest));
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List all comments in the current playlist.
	public Page<CommentPlaylistModel> listCommentsInPlaylist(int playlistId, int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > commentMaxPageSize) {
			pageSize = commentDefaultSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<CommentPlaylistModel> result;
		result = commentPlaylistRepository.listAllComments(playlistId, sendPageRequest);
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List all comments in the current track.
	public Page<CommentTrackModel> listCommentsInTrack(int trackId, int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > commentMaxPageSize) {
			pageSize = commentDefaultSize;
		}
		Pageable sendPageRequest = PageRequest.of(page, pageSize);
		Page<CommentTrackModel> result;
		result = commentTrackRepository.listAllComments(trackId, sendPageRequest);
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// Post new comment in playlist.
	public CommentPlaylistModel postNewPlaylistComment(CommentForm form, HttpServletRequest request) {
		if (form.getPlaylistId() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] Playlist ID in the form can't be 0 or less.");
		}

		UserAccountModel user = generalFunctionController.getUserAccount(request);
		PlaylistModel playlist = playlistRepository.findById(form.getPlaylistId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The playlist with ID " + form.getPlaylistId()
								+ " does not exist."));

		CommentPlaylistModel comment = new CommentPlaylistModel();
		comment.setComment(form.getComment());
		comment.setPlaylist(playlist);
		comment.setUser(user);
		comment.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		comment = commentPlaylistRepository.save(comment);
		return comment;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// Post new comment in track.
	public CommentTrackModel postNewTrackComment(CommentForm form, HttpServletRequest request) {
		if (form.getTrackId() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] Track ID in the form can't be 0 or less.");
		}

		UserAccountModel user = generalFunctionController.getUserAccount(request);
		TracksModel track = tracksRepository.findById(form.getTrackId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND,
						"[ BROWSE_NO_RECORD_EXISTS ] The track with ID " + form.getTrackId() + " does not exist."));

		CommentTrackModel comment = new CommentTrackModel();
		comment.setComment(form.getComment());
		comment.setTrack(track);
		comment.setUser(user);
		comment.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
		comment = commentTrackRepository.save(comment);
		return comment;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// Delete playlist comment
	public void deletePlaylistComment(int commentId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentPlaylistModel comment = commentPlaylistRepository.findById(commentId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());
		commentPlaylistRepository.deleteById(commentId);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// Delete track comment
	public void deleteTrackComment(int commentId, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentTrackModel comment = commentTrackRepository.findById(commentId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());
		commentTrackRepository.deleteById(commentId);
	}

	// list all of my comment.

	// list all of user comment.
}
