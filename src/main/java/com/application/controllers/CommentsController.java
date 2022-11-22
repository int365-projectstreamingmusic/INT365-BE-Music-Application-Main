package com.application.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.ActionForm;
import com.application.entities.submittionforms.CommentForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.CommentPlaylistRepository;
import com.application.repositories.CommentTrackRepository;
import com.application.repositories.PlaylistRepository;
import com.application.repositories.ReportGroupRepository;
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
	private ReportGroupRepository reportGroupRepository;

	@Autowired
	private GeneralFunctionController generalFunctionController;
	@Autowired
	private ActionHistoryController actionHistoryController;

	private int commentDefaultSize = 100;
	private int commentMaxPageSize = 500;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// List comments of the user.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Map<String, Object> listMyComment(int page, int pageSize, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		return listUserComment(owner.getAccountId(), page, pageSize);
	}

	public Map<String, Object> listUserComment(int userId, int page, int pageSize) {
		Page<CommentTrackModel> trackCommentRaw = commentTrackRepository.listAllCommentsByUser(userId,
				getPageRequest(page, pageSize));
		Page<CommentPlaylistModel> playlistCommentRaw = commentPlaylistRepository.listAllCommentsByUser(userId,
				getPageRequest(page, pageSize));

		Map<String, Object> result = new HashMap<>();

		if (trackCommentRaw.getTotalElements() <= 0 && playlistCommentRaw.getTotalElements() < +0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ BROWSE_NO_RECORD_EXISTS ] You are not having any comment for a moment.");
		}

		if (trackCommentRaw.getTotalElements() > 0) {
			List<CommentTrackModel> trackCommentList = new ArrayList<>();
			for (int i = 0; i < trackCommentRaw.getContent().size(); i++) {
				CommentTrackModel current = trackCommentRaw.getContent().get(i);
				current.setUser(null);
				trackCommentList.add(current);
			}
			result.put("commentTrack", new PageImpl<>(trackCommentList, getPageRequest(page, pageSize),
					playlistCommentRaw.getTotalElements()));
		} else {
			result.put("commentTrack", trackCommentRaw);
		}

		if (playlistCommentRaw.getTotalElements() > 0) {
			List<CommentPlaylistModel> playlistCommentList = new ArrayList<>();
			for (int i = 0; i < playlistCommentRaw.getContent().size(); i++) {
				CommentPlaylistModel current = playlistCommentRaw.getContent().get(i);
				current.setUser(null);
				playlistCommentList.add(current);
			}
			result.put("commentTrack", new PageImpl<>(playlistCommentList, getPageRequest(page, pageSize),
					playlistCommentRaw.getTotalElements()));
		} else {
			result.put("commmentPlaylist", playlistCommentRaw);
		}

		return result;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List all comments in the current playlist.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Page<CommentPlaylistModel> listCommentsInPlaylist(int playlistId, int page, int pageSize) {
		Page<CommentPlaylistModel> result;
		result = commentPlaylistRepository.listAllComments(playlistId, getPageRequest(page, pageSize));
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.OK,
					"[ BROWSE_NO_RECORD_EXISTS ] This playlist has no comment.");
		}
		return result;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// List all comments in the current track.
	// EXCEPTION | 40001 | BROWSE_NO_RECORD_EXISTS
	public Page<CommentTrackModel> listCommentsInTrack(int trackId, int page, int pageSize) {
		Page<CommentTrackModel> result;
		result = commentTrackRepository.listAllComments(trackId, getPageRequest(page, pageSize));
		if (result.getTotalElements() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.OK,
					"[ BROWSE_NO_RECORD_EXISTS ] This track has no comment.");
		}
		return result;
	}

	// Get page request.
	private PageRequest getPageRequest(int page, int pageSize) {
		if (page < 0) {
			page = 0;
		}
		if (pageSize < 1 || pageSize > commentMaxPageSize) {
			pageSize = commentDefaultSize;
		}
		return PageRequest.of(page, pageSize);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
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
	// DB-V6 OK!
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
	// DB-V6 OK!
	// Edit track comment
	public CommentTrackModel editTrackComment(CommentForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentTrackModel comment = commentTrackRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());
		comment.setComment(form.getComment());
		commentTrackRepository.editComment(comment.getId(), comment.getComment());
		return comment;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Edit Playlist comment
	public CommentPlaylistModel editPlaylistComment(CommentForm form, HttpServletRequest request) {
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentPlaylistModel comment = commentPlaylistRepository.findById(form.getId())
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());
		comment.setComment(form.getComment());
		commentPlaylistRepository.editComment(comment.getId(), comment.getComment());
		return comment;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Delete track comment
	// NOTE : By user
	public String deleteTrackComment(int commentId, HttpServletRequest request) {
		// General
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentTrackModel comment = commentTrackRepository.findById(commentId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());

		// Check if there is ongoing report on this comment.
		if (reportGroupRepository.existsByTrackComment(commentId) == 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_HAS_REPORT, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_HAS_REPORT ] This track has an ongoing report to be proceeded.");
		}

		// Delete and save history.
		commentTrackRepository.deleteById(commentId);
		String message = owner.getUsername() + " deleted their comment on a track ID " + comment.getId();
		actionHistoryController.addNewRecord(new ActionForm(owner, commentId, 401, message));

		return message;
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Delete playlist comment
	// NOTE : By user
	public String deletePlaylistComment(int commentId, HttpServletRequest request) {
		// General
		UserAccountModel owner = generalFunctionController.getUserAccount(request);
		CommentPlaylistModel comment = commentPlaylistRepository.findById(commentId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS,
						HttpStatus.NOT_FOUND, "[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
		generalFunctionController.checkOwnerShipForRecord(owner.getAccountId(), comment.getUser().getAccountId());

		// Check if there is ongoing report on this comment.
		if (reportGroupRepository.existsByPlaylistComment(commentId) == 1) {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_HAS_REPORT, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_HAS_REPORT ] This track has an ongoing report to be proceeded.");
		}

		// Delete and save history.
		commentPlaylistRepository.deleteById(commentId);
		String message = owner.getUsername() + " deleted their comment on a playlist ID " + comment.getId();
		actionHistoryController.addNewRecord(new ActionForm(owner, commentId, 401, message));
		return message;

	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Delete Track or playlist comment
	// NOTE : By staff
	// commentType | 2001 | Track Comment
	// commentType | 2002 | Playlist Comment
	public String deleteComment(int commentId, int commentType, String reason, HttpServletRequest request) {
		UserAccountModel staff = generalFunctionController.getUserAccount(request);
		String message = deleteComment(commentId, commentType, reason, staff);
		return message;
	}

	public String deleteComment(int commentId, int commentType, String reason, UserAccountModel staff) {
		String message = "";
		switch (commentType) {
		case 2001: {
			CommentTrackModel comment = commentTrackRepository.findById(commentId).orElseThrow(
					() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
							"[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
			UserAccountModel owner = comment.getUser();
			commentTrackRepository.deleteById(commentId);

			message = "Staff, " + staff.getUsername() + " deleted a user comment of " + owner.getUsername()
					+ " from the track ID " + comment.getTrack().getId()
					+ (reason.length() <= 0 ? "" : " Reason : " + reason);
			actionHistoryController.addNewRecord(new ActionForm(staff, commentId, 402, message));
			break;
		}
		case 2002: {
			CommentPlaylistModel comment = commentPlaylistRepository.findById(commentId).orElseThrow(
					() -> new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
							"[ BROWSE_NO_RECORD_EXISTS ] The comment with this ID does not exist."));
			UserAccountModel owner = comment.getUser();
			commentPlaylistRepository.deleteById(commentId);

			message = "Staff, " + staff.getUsername() + " deleted a user comment of " + owner.getUsername()
					+ " from the playlist ID " + comment.getPlaylist().getId()
					+ (reason.length() <= 0 ? "" : " Reason : " + reason);
			actionHistoryController.addNewRecord(new ActionForm(staff, commentId, 402, message));
			break;
		}
		default: {
			throw new ExceptionFoundation(EXCEPTION_CODES.RECORD_INVALID_STATUS, HttpStatus.I_AM_A_TEAPOT,
					"[ RECORD_INVALID_STATUS ] Comment type is invalid. Please check the comment type ID.");
		}
		}
		return message;
	}

}
