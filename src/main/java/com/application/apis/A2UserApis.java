package com.application.apis;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.CommentsController;
import com.application.controllers.PlayHistoryController;
import com.application.controllers.PlaylistController;
import com.application.controllers.TrackMarkingController;
import com.application.controllers.UserProfileController;
import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.PlayHistoryModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.PlaylistTrackListModel;
import com.application.entities.models.TrackMarkingModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.CommentForm;
import com.application.entities.submittionforms.PlaylistForm;
import com.application.entities.submittionforms.PlaylistOutput;
import com.application.entities.submittionforms.TrackMarkingForm;
import com.application.entities.submittionforms.UserProfileForm;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

@RestController
@RequestMapping("/api/user/")
public class A2UserApis {

	private static String mapping = "/api/user/";

	@Autowired
	private PlaylistController playlistController;
	@Autowired
	private UserProfileController userProfileController;
	@Autowired
	private TrackMarkingController trackMarkingController;
	@Autowired
	private PlayHistoryController playHistoryController;
	@Autowired
	private CommentsController commentsController;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : ListMyCreatedPlaylist
	@GetMapping("playlist")
	public ResponseEntity<Page<PlaylistModel>> listMyOwnedPlaylist(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.getMyPlaylist(page, pageSize, searchContent, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Get my playlist by ID.
	@GetMapping("playlist/{id}")
	public ResponseEntity<PlaylistOutput> GetMyOwnedPlaylistById(@PathVariable int id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "0") int pageSize,
			@RequestParam(defaultValue = "") String searchContent, HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.getPlaylistById(id, page, pageSize, searchContent, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Create Playlist
	@PostMapping(value = "playlist", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE,
			MediaType.IMAGE_PNG_VALUE })
	public ResponseEntity<PlaylistModel> createNewPlaylist(@RequestPart(required = true) PlaylistForm form,
			@RequestPart(required = false) MultipartFile image, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist").toString());
		return ResponseEntity.created(uri).body(playlistController.createMyPlaylist(form, image, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Edit my playlist
	@PutMapping(value = "playlist")
	public ResponseEntity<PlaylistModel> editPlaylist(@RequestPart(required = true, name = "form") PlaylistForm form,
			@RequestPart(required = false, name = "image") MultipartFile image, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist").toString());
		return ResponseEntity.created(uri).body(playlistController.editMyPlaylist(form, image, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Upload playlist thumbnail
	@PutMapping(value = "playlist/thumbnail/{trackId}")
	public ResponseEntity<HttpStatus> uploadThumbnail(@PathVariable int trackId, @RequestPart MultipartFile image,
			HttpServletRequest request) {
		playlistController.uploadeThumbnail(trackId, image, request);
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(mapping + "playlist/thumbnail/" + trackId).toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Switch between private and public.
	@PutMapping("playlist/visibility")
	public ResponseEntity<String> togleVisibility(@RequestParam(required = true) int id, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist/visibility").toString());
		return ResponseEntity.created(uri).body(playlistController.switchPlaylistStatus(id, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Delete playlist
	@DeleteMapping("playlist")
	public ResponseEntity<HttpStatus> deletePlaylist(@RequestParam int id, HttpServletRequest request) {
		playlistController.deletePlaylist(id, request);
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Add tracks to playlist : Can only add to 1 playlist at a time.
	@PutMapping("playlist/add-track")
	public ResponseEntity<List<PlaylistTrackListModel>> addTrackToPlaylist(@RequestBody PlaylistForm form,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.addTrackToPlaylist(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Remove tracks from playlist : Can only add to 1 playlist.
	@PutMapping("playlist/remove-track")
	public ResponseEntity<List<PlaylistTrackListModel>> removeTrackFromPlaylist(@RequestBody PlaylistForm form,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.removeTrackFromPlaylist(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ACCOUNT : Get my profile
	@GetMapping("myProfile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest request) {
		return ResponseEntity.ok().body(userProfileController.getMyProfile(request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// ACCOUNT : Edit my profile.
	@PutMapping(value = "myProfile")
	public ResponseEntity<UserAccountModel> editMyProfile(
			@RequestPart(required = true, name = "profile") UserProfileForm profile,
			@RequestPart(required = false, name = "profileImage") MultipartFile profileImage,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "myProfile").toString());
		return ResponseEntity.created(uri)
				.body(userProfileController.editBasicProfileInfo(profile, profileImage, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Comment
	// ---------------------

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK!
	// COMMENT : List my comments.
	@GetMapping("comment")
	public ResponseEntity<Map<String, Object>> listComment(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, HttpServletRequest request) {
		return ResponseEntity.ok().body(commentsController.listMyComment(page, pageSize, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK
	// COMMENT TRACK : Post new comment to an existing track.
	@PostMapping("comment/track")
	public ResponseEntity<CommentTrackModel> postNewTrackComment(@RequestBody(required = true) CommentForm form,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/track").toString());
		return ResponseEntity.created(uri).body(commentsController.postNewTrackComment(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK
	// COMMENT TRACK : Delete by Id
	@DeleteMapping("comment/track")
	public ResponseEntity<HttpStatus> deleteTrackComment(@RequestParam(required = true) int commentId,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/track").toString());
		commentsController.deleteTrackComment(commentId, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK
	// COMMENT PLAYLIST: Post new comment to an existing playlist.
	@PostMapping("comment/playlist")
	public ResponseEntity<CommentPlaylistModel> postNewPlaylistComment(@RequestBody(required = true) CommentForm form,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/playlist").toString());
		return ResponseEntity.created(uri).body(commentsController.postNewPlaylistComment(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.2 OK
	// COMMENT PLAYLIST: Delete by Id
	@DeleteMapping("comment/playlist")
	public ResponseEntity<HttpStatus> deletePlaylistComment(@RequestParam(required = true) int commentId,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/playlist").toString());
		commentsController.deletePlaylistComment(commentId, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// COMMENT TRACK : Edit comment
	@PutMapping("comment/track")
	public ResponseEntity<CommentTrackModel> editTrackComment(@RequestBody(required = true) CommentForm form,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/track").toString());
		return ResponseEntity.created(uri).body(commentsController.editTrackComment(form, request));
	}

	// COMMENT PLAYLIST : Edit comment
	@PutMapping("comment/playlist")
	public ResponseEntity<CommentPlaylistModel> editPlaylistComment(@RequestBody(required = true) CommentForm form,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "comment/track").toString());
		return ResponseEntity.created(uri).body(commentsController.editPlaylistComment(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Playground
	// ---------------------

	// PLAYGROUND : List all track in playground.
	@GetMapping("Playground")
	public ResponseEntity<Page<UserTrackMarkingModel>> getMyPlayground(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackMarkingController.listTrackByTrackMarkingAndUserAccountId(page, pageSize,
				1002, searchContent, request));
	}

	// PLAYGROUND : Add one to playground.
	@PostMapping("Playground")
	public ResponseEntity<UserTrackMarkingModel> addTrackToMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.addNewTrackMarking(trackId, 1002, request));
	}

	// PLAYGROUND : Remove one from playground.
	@DeleteMapping("Playground")
	public ResponseEntity<HttpStatus> deleteTrackFromMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground").toString());
		trackMarkingController.removeTrackMarking(trackId, 1002, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// PLAYGROUND : Delete all from playground.
	@DeleteMapping("Playground/clear")
	public ResponseEntity<HttpStatus> clearMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground/clear").toString());
		trackMarkingController.clearTrackInPlayGround(request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// PLAYGROUND : Add many to playground.
	@PostMapping("Playground/add")
	public ResponseEntity<List<UserTrackMarkingModel>> addManyTracksToPlayground(
			@RequestBody(required = true) TrackMarkingForm form, HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground/add").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.addManyToPlayground(form, request));
	}

	// PLAYGROUND : delete many to playground.
	@DeleteMapping("Playground/remove")
	public ResponseEntity<HttpStatus> removeManyFromPlayground(@RequestBody(required = true) TrackMarkingForm form,
			HttpServletRequest request) {
		trackMarkingController.removeManyFromPlayground(form, request);
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground/remove").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// FAVORITE : delete may from favrite
	@DeleteMapping("Favorite/remove")
	public ResponseEntity<HttpStatus> removeManyFromFavorite(@RequestBody(required = true) TrackMarkingForm form,
			HttpServletRequest request) {
		trackMarkingController.removeManyFromFavorite(form, request);
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground/remove").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Favourite
	// ---------------------

	// FAVORITE : List my favorite tracks.
	@GetMapping("Favorite")
	public ResponseEntity<Page<UserTrackMarkingModel>> getMyFavoriteList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackMarkingController.listTrackByTrackMarkingAndUserAccountId(page, pageSize,
				1001, searchContent, request));
	}

	// FAVORITE : Add one favorite.
	@PostMapping("Favorite")
	public ResponseEntity<UserTrackMarkingModel> addMyFavoriteTrack(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Favorite").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.addNewTrackMarking(trackId, 1001, request));
	}

	// FAVORITE : Delete one favorite.
	@DeleteMapping("Favorite")
	public ResponseEntity<HttpStatus> deleteTrackFromMyFavorite(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Favorite").toString());
		trackMarkingController.removeTrackMarking(trackId, 1001, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// FAVORITE : Add many favorite.
	@PostMapping("Favorite/add")
	public ResponseEntity<List<UserTrackMarkingModel>> addManyTracksToFavorite(
			@RequestBody(required = true) TrackMarkingForm form, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Favorite/add").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.AddManyFavorite(form, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// History
	// ---------------------

	@GetMapping("history/LastVisited")
	public ResponseEntity<List<PlayHistoryModel>> getLastVisited(@RequestParam(defaultValue = "6") int numberOfRecord,
			HttpServletRequest request) {
		if (numberOfRecord > 120) {
			numberOfRecord = 6;
		}
		return ResponseEntity.ok().body(playHistoryController.listMyLastVisit(numberOfRecord, request));
	}

	@GetMapping("history/MyHistory")
	public ResponseEntity<Page<PlayHistoryModel>> getMyHistory(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playHistoryController.getMyHistory(page, pageSize, searchContent, request));
	}

	@GetMapping("history/GetSpecifiedRecord")
	public ResponseEntity<PlayHistoryModel> getSpecifiedRecord(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playHistoryController.getRecordsByUserIdAndTrackId(trackId, request));
	}

	@DeleteMapping("history/Clear")
	public ResponseEntity<HttpStatus> clearHistory(@RequestParam(defaultValue = "0") int inPassMinutes,
			HttpServletRequest request) {
		if (inPassMinutes == 0) {
			playHistoryController.clearHistory(request);
		} else {
			playHistoryController.clearHistoryInThePassHoures(request, inPassMinutes);
		}
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "history/Clear").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	@DeleteMapping("history/DeleteSpecificHistory")
	public ResponseEntity<HttpStatus> deleteSpecificHistory(@RequestParam(defaultValue = "0") int historyId,
			@RequestParam(defaultValue = "0") int trackId, HttpServletRequest request) {
		if ((historyId == 0 && trackId == 0) || (historyId != 0 && trackId != 0)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_IMPOSSIBLE, HttpStatus.I_AM_A_TEAPOT,
					"[ BROWSE_IMPOSSIBLE ] You can't leave both parameters NULL or send both parameters to the API. You must onli pick either track ID or History ID.");
		}

		if (trackId != 0) {
			playHistoryController.deleteRecordByUserIdAndTrackId(trackId, request);
		} else {
			playHistoryController.deleteRecordById(historyId, request);
		}
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path(mapping + "history/DeleteSpecificHistory").toString());
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

}
