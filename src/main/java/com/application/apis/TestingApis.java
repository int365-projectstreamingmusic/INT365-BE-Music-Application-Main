package com.application.apis;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.application.controllers.ArtistController;
import com.application.controllers.PlayHistoryController;
import com.application.controllers.TrackCountController;
import com.application.controllers.TrackManagerController;
import com.application.controllers.TrackMarkingController;
import com.application.entities.models.ArtistsModel;
import com.application.entities.models.ArtistsTrackModel;
import com.application.entities.models.PlayHistoryModel;
import com.application.entities.models.UserTrackMarkingModel;
import com.application.entities.submittionforms.ArtistTrackForm;
import com.application.entities.submittionforms.ArtistsEditForm;
import com.application.repositories.PlayHistoryRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.ValidatorServices;

@RestController
@RequestMapping("test/")
public class TestingApis {

	@Autowired
	private TrackCountController trackCountController;

	// VIEW COUNT

	@GetMapping("AddCustomTrackCount")
	public ResponseEntity<Map<String, Object>> addCustomTrackCount(@RequestParam int trackId, @RequestParam int nWeek) {
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/manager/trackcount/addMyArtistToTrack").toString());
		return ResponseEntity.created(uri).body(trackCountController.addCustomViewCount(trackId, nWeek));
	}

}
