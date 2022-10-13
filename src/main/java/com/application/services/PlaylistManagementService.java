package com.application.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.entities.models.PlaylistModel;
import com.application.entities.models.UserAccountModel;
import com.application.entities.submittionforms.NewPlaylistForm;
import com.application.repositories.PlaylistRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class PlaylistManagementService {

	@Autowired
	PlaylistRepository playlistRepository;

	// List playlist by username

	// List playlist randomly

	// List playlist by popularity

	// List playlist by newest

	// Create New Playlist
	public void createNewPlaylist(NewPlaylistForm newPlaylist, HttpServletRequest request) {
		String username = JwtTokenUtills.getUserNameFromToken(request);

		UserAccountModel createdBy = new UserAccountModel();

		PlaylistModel createdPlaylist = new PlaylistModel();
		createdPlaylist.setPlaylistName(newPlaylist.getPlaylist_name());
		createdPlaylist.setPlaylistDesc(newPlaylist.getPlaylist_desc());
	}

	// Edit playlist description
	public void updatePlaylistDescription(String description) {

	}

	// Edit playlist thumbnail.
	public void upfatePlaylistThumbnail(String thunbnail) {

	}

	// ---------------------------------------
	// List plalist by this month popularity

	// List playlsit by this week popularity

}
