package com.application.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.application.utilities.JwtTokenUtills;

@Service
public class TrackController {

	// Put track into playground.
	public void addTrackToPlayground(int trackId, HttpServletRequest request) {
		String addedBy = JwtTokenUtills.getUserNameFromToken(request);
		
	}

	// Put track into specific playlist.

	// Remove track from playground.

	// Remove track from playlist.

	// Favorite track

	// Unlike track

	// Get all tracks in the playlist by page.

	// Get all favorite track by page.

	// Get all track from playground by page.
	
	// Report Track

}
