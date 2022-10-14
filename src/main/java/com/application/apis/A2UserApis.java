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
import com.application.entities.models.UserAccountModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.PlaylistForm;
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

	// PLAYLIST : ListMyCreatedPlaylist
	@GetMapping("playlist")
	public ResponseEntity<Page<PlaylistModel>> listMyOwnedPlaylist(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "") String searchContent,
			HttpServletRequest request) {
		return ResponseEntity.ok().body(playlistController.getMyPlaylist(page, pageSize, searchContent, request));
	}

	// PLAYLIST : Create Playlist
	@PostMapping("playlist/create")
	public ResponseEntity<Page<PlaylistModel>> createNewPlaylist(@RequestBody PlaylistForm newPlaylist,
			HttpServletRequest request) {
		URI uri = URI.create(
				ServletUriComponentsBuilder.fromCurrentContextPath().path(mapping + "playlist/create").toString());
		return ResponseEntity.created(uri).body(null);
	}

	// ---------------------
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

	// ---------------------
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

	// ---------------------
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

	// ---------------------
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
