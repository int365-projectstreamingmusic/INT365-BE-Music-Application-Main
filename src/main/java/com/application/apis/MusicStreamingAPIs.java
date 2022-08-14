package com.application.apis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.MusicStreamingController;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/streaming/")
public class MusicStreamingAPIs {

	@Autowired
	private MusicStreamingController musicStreamingController;

	// streamContent
	@GetMapping("getContent/{type}/{track}")
	public Mono<ResponseEntity<byte[]>> getContent(
			@RequestHeader(value = "Range", required = false) String httpByteRange, @PathVariable("type") String type,
			@PathVariable("track") String track) {
		try {
			return Mono.just(musicStreamingController.getTrack(type, track, httpByteRange));
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.FEATURE_MISS_USED, HttpStatus.BAD_REQUEST,
					"[ getContent ] This function works as normal but you must call it on your media player, not directly called to an API like this. ");
		}
	}

	// getStatObject
	@GetMapping("getStatObject/{type}/{trackName}")
	public ResponseEntity<Map<String, Object>> getStatObject(@PathVariable(value = "trackName") String trackName,
			@PathVariable(value = "type") String type) {
		return ResponseEntity.ok().body(musicStreamingController.getStatObject(type, trackName));
	}
}
