package com.application.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("generalsetting.properties")
public class GeneralPageContentController {

	// GenerateFrontPageForUser
	public Map<?, ?> generateHomepageContents() {
		Map<String, Object> result = new HashMap<>();
		result.put("track-list", "");
		result.put("top-five-popular", "");
		result.put("new-arival", "");
		result.put("recommended-for-you", "");
		return result;
	}

	// GenerateArtistContentList
	public Map<?, ?> generateContentList() {
		Map<String, Object> result = new HashMap<>();
		
		return null;
	}

}
