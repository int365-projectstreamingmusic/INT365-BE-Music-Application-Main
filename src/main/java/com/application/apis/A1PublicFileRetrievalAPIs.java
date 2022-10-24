package com.application.apis;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.controllers.FileLinkRelController;
import com.application.controllers.MusicStreamingController;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/streaming/")
public class A1PublicFileRetrievalAPIs {

	@Autowired
	private MusicStreamingController musicStreamingController;
	@Autowired
	private FileLinkRelController fileLinkRelController;

	// Ok!
	// will add the history and count to the user if the token exist.
	@PutMapping("entrance/{track}")
	public void entrance(@PathVariable String track, HttpServletRequest request) {
		musicStreamingController.trackEntrance(track, request);
	}

	// OK!
	// streamContent
	@GetMapping("getContent/{track}")
	public Mono<ResponseEntity<byte[]>> getContent(
			@RequestHeader(value = "Range", required = false) String httpByteRange,
			@PathVariable("track") String trackFile, HttpServletRequest request) {
		try {
			return Mono.just(musicStreamingController.getTrack(trackFile, httpByteRange, request));
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.FEATURE_MISS_USED, HttpStatus.BAD_REQUEST,
					"[ getContent ] This function works as normal but you must call it on your media player, not directly called to an API like this. ");
		}
	}

	// OK!
	// getStatObject
	@GetMapping("getStatObject/{type}/{trackName}")
	public ResponseEntity<Map<String, Object>> getStatObject(@PathVariable(value = "trackName") String trackName,
			@PathVariable(value = "type") String type) {
		return ResponseEntity.ok().body(musicStreamingController.getStatObject(type, trackName));
	}

	// OK!
	// ImageRetrievalService
	@GetMapping(value = "image/{imageName}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE,
			MediaType.IMAGE_GIF_VALUE })
	public ResponseEntity<Resource> imageRetrievalService(@PathVariable String imageName) {
		return ResponseEntity.ok().body(fileLinkRelController.retriveImageByName(imageName));
	}

}
