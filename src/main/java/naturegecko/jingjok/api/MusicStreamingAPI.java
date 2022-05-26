 package naturegecko.jingjok.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.minio.StatObjectResponse;
import naturegecko.jingjok.configurations.EnumConfig.MINIO_DIRECTORY;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;
import naturegecko.jingjok.services.MinioStorageService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/trackstreaming/")
public class MusicStreamingAPI {

	@Autowired
	private MinioStorageService minioStorageService;
	private static final long streamrange = 500000;

	@GetMapping("getcontent/{tracklocation}")
	public Mono<ResponseEntity<byte[]>> getContent(
			@RequestHeader(value = "Range", required = false) String httpByteRange,
			@PathVariable("tracklocation") String trackName) {
		return Mono.just(
				getTrackContent(MINIO_DIRECTORY.TRACKS_MUSICS.getDestinationDirectory() + trackName, httpByteRange));
	}

	@GetMapping("getinfo/{tracklocation}")
	public ResponseEntity<String> getInfo() {
		throw new ExceptionFoundation(EXCEPTION_CODES.CORE_NOT_IMPLEMENTED,
				"[ NOPE ] the method \' getInfo \' is not implemented.");
	}

	private ResponseEntity<byte[]> getTrackContent(String trackFileLocation, String range) {
		StatObjectResponse statObject = minioStorageService.getStatObject(trackFileLocation);
		long byteRangeStart = Long.parseLong(range.split("-")[0].substring(6));
		long byteRangeEnd;
		byte[] contentData;
		String fileType = trackFileLocation.substring(trackFileLocation.lastIndexOf(".") + 1);
		long trackSize = statObject.size();

		if (streamrange > trackSize - byteRangeStart) {
			byteRangeEnd = trackSize - 1;
		} else {
			byteRangeEnd = byteRangeStart + streamrange;
		}

		String contentLength = String.valueOf((byteRangeEnd - byteRangeStart) + 1);

		try {
			contentData = minioStorageService
					.trackRetrivelByByteRangeService(trackFileLocation, byteRangeStart, byteRangeEnd).readAllBytes();
		} catch (IOException ecc) {
			System.out.println("[ Track byte retrival service failed. ]");
			System.out.println("Start from : " + byteRangeStart);
			System.out.println("End from : " + byteRangeEnd);
			System.out.println("actuall size : " + trackSize);

			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_METHOD_FAILED, "[ METHOD FAILED ] " + ecc.getMessage());
		}

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).header("Content-Type", "audio/" + fileType)
				.header("Accept-Ranges", "bytes").header("Content-Length", contentLength)
				.header("Content-Range", "bytes " + byteRangeStart + "-" + byteRangeEnd + "/" + trackSize)
				.body(contentData);
	}
}
