package com.application.apis;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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

import com.application.controllers.PlayHistoryController;
import com.application.controllers.PlaylistController;
import com.application.controllers.TrackMarkingController;
import com.application.controllers.UserProfileController;
import com.application.entities.models.PlayHistoryModel;
import com.application.entities.models.PlaylistModel;
import com.application.entities.models.PlaylistTrackListModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.PlaylistForm;
import com.application.entities.submittionforms.PlaylistOutput;
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
		return ResponseEntity.ok().body(playlistController.getPlaylistByID(id, page, pageSize, searchContent, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Create Playlist
	@PostMapping("playlist")
	public ResponseEntity<PlaylistModel> createNewPlaylist(@RequestPart(required = true) PlaylistForm newPlaylist,
			@RequestPart(required = false) MultipartFile imageFile, HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist").toString());
		return ResponseEntity.created(uri).body(playlistController.createMyPlaylist(newPlaylist, imageFile, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Edit my playlist
	@PutMapping("playlist")
	public ResponseEntity<PlaylistModel> editPlaylist(@RequestBody(required = true) PlaylistForm detail,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist").toString());
		return ResponseEntity.created(uri).body(playlistController.editMyPlaylist(detail, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V5.1 OK!
	// PLAYLIST : Upload playlist thumbnail
	@PutMapping("playlist/thumbnail/{trackId}")
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

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// User profile Api
	// ---------------------
	@GetMapping("myProfile")
	public ResponseEntity<UserAccountModel> getMyProfile(HttpServletRequest request) {
		return ResponseEntity.ok().body(userProfileController.getMyProfile(request));
	}

	@PutMapping("myProfile")
	public ResponseEntity<UserAccountModel> editMyProfile(@RequestBody UserProfileForm newInfo,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "myProfile").toString());
		return ResponseEntity.created(uri).body(userProfileController.editBasicProfileInfo(newInfo, request));
	}

	@PutMapping("profile-image")
	public ResponseEntity<String> updateProfileImage(@RequestPart MultipartFile profileImage,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path("api/profile/profile-image").toString());
		return ResponseEntity.created(uri).body(userProfileController.setNewUserProfileImage(profileImage, request));
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Playground
	// ---------------------

	@GetMapping("Playground")
	public ResponseEntity<Page<UserTrackMarkingModel>> getMyPlayground(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackMarkingController.listTrackByTrackMarkingAndUserAccountId(page, pageSize,
				1002, searchContent, request));
	}

	@PostMapping("Playground")
	public ResponseEntity<UserTrackMarkingModel> addTrackToMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.addNewTrackMarking(trackId, 1002, request));
	}

	@DeleteMapping("Playground")
	public ResponseEntity<HttpStatus> deleteTrackFromMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground").toString());
		trackMarkingController.removeTrackMarking(trackId, 1002, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	@DeleteMapping("Playground/clear")
	public ResponseEntity<HttpStatus> clearMyPlayground(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Playground/clear").toString());
		trackMarkingController.clearTrackInPlayGround(request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
	}

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// Favourite
	// ---------------------

	@GetMapping("Favorite")
	public ResponseEntity<Page<UserTrackMarkingModel>> getMyFavoriteList(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(trackMarkingController.listTrackByTrackMarkingAndUserAccountId(page, pageSize,
				1001, searchContent, request));
	}

	@PostMapping("Favorite")
	public ResponseEntity<UserTrackMarkingModel> addMyFavoriteTrack(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Favorite").toString());
		return ResponseEntity.created(uri).body(trackMarkingController.addNewTrackMarking(trackId, 1001, request));
	}

	@DeleteMapping("Favorite")
	public ResponseEntity<HttpStatus> deleteTrackFromMyFavorite(@RequestParam(required = true) int trackId,
			HttpServletRequest request) {
		URI uri = URI
				.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "Favorite").toString());
		trackMarkingController.removeTrackMarking(trackId, 1001, request);
		return ResponseEntity.created(uri).body(HttpStatus.CREATED);
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