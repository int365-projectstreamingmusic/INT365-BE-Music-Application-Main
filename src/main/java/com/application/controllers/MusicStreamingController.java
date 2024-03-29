package com.application.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.TracksRepository;
import com.application.utilities.JwtTokenUtills;
import com.application.utilities.MinioStorageService;

import io.minio.StatObjectResponse;

@Service
@PropertySource("generalsetting.properties")
public class MusicStreamingController {

	private static final long streamRange = 500000;

	@Autowired
	private MinioStorageService minioStorageService;

	@Autowired
	private PlayHistoryController playHistoryController;
	@Autowired
	private TrackStatisticController trackStatisticController;
	@Autowired
	private JwtTokenUtills tokenUtills;

	@Autowired
	private TracksRepository tracksRepository;

	@Value("${minio.storage.track.music}")
	private String trackMusicLocation;

	@Value("${minio.storage.track.sound}")
	private String trackSoundLocation;

	// This will give user a history and view count when entered the track.
	public void trackEntrance(String trackFile, HttpServletRequest request) {
		int trackId = tracksRepository.getIdFromFileName(trackFile);
		if (request.getHeader("Authorization") != null) {
			int userId = tokenUtills.getUserAccountFromToken(request).getAccountId();
			playHistoryController.InsertOrUpdateHistory(userId, trackId);
		}
		trackStatisticController.increateViewCount(trackId);
	}

	// This will call a track file and send it to the user.
	// This will also save a history if there is a token.
	public ResponseEntity<byte[]> getTrack(String trackFile, String range, HttpServletRequest request) {
		return getTrackContentByRange("tracks/musics/" + trackFile, range);
	}

	// OK!
	// getStatObject
	public Map<String, Object> getStatObject(String type, String trackName) {
		String trackNameLocation = getPathFromType(type) + trackName;
		try {
			Map<String, Object> statObjectResult = new HashMap<String, Object>();
			StatObjectResponse statObject = minioStorageService.getStatObjectFromObject(trackNameLocation);
			statObjectResult.put("Bucket", statObject.bucket());
			statObjectResult.put("LastModified", statObject.lastModified().toString());
			statObjectResult.put("Object", statObject.object());
			statObjectResult.put("Size", statObject.size());
			statObjectResult.put("medisType", statObject.contentType());
			return statObjectResult;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.BROWSE_NO_RECORD_EXISTS, HttpStatus.NOT_FOUND,
					"[ getStatObject ] " + trackNameLocation + " is unreachable.");
		}
	}

	private String getPathFromType(String type) {
		String location;
		switch (type) {
		case "music": {
			location = trackMusicLocation;
			return location;
		}
		case "sound": {
			location = trackSoundLocation;
			return location;
		}
		default:
			throw new ExceptionFoundation(EXCEPTION_CODES.FEATURE_MISS_USED, HttpStatus.BAD_REQUEST,
					"[ getTrack ] You have an unexpeted value that is not allowed in this function. ( Because this path does not exist, see the backend manual for more information. )");
		}
	}

	// OK!
	// getTrackContentByRange
	private ResponseEntity<byte[]> getTrackContentByRange(String trackFileLocation, String range) {
		StatObjectResponse statObject = minioStorageService.getStatObjectFromObject(trackFileLocation);
		long byteRangeStart = Long.parseLong(range.split("-")[0].substring(6));
		long byteRangeEnd;
		byte[] requestedByteRange;
		String fileType = trackFileLocation.substring(trackFileLocation.lastIndexOf(".") + 1);
		long trackSize = statObject.size();

		if (streamRange > trackSize - byteRangeStart) {
			byteRangeEnd = trackSize - 1;
		} else {
			byteRangeEnd = byteRangeStart + streamRange;
		}

		String contentLength = String.valueOf((byteRangeEnd - byteRangeStart) + 1);

		try {
			requestedByteRange = minioStorageService
					.trackRetrivelByByteRangeService(trackFileLocation, byteRangeStart, byteRangeEnd).readAllBytes();
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).header("Content-Type", "audio/" + fileType)
					.header("Accept-Ranges", "bytes").header("Content-Length", contentLength)
					.header("Content-Range", "bytes " + byteRangeStart + "-" + byteRangeEnd + "/" + trackSize)
					.header("Current-bytes", "" + byteRangeStart).body(requestedByteRange);
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_METHOD_FAILED, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ getTrackContentByRange ] Streaming failed.");
		}

	}

}
