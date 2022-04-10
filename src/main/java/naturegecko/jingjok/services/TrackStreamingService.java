package naturegecko.jingjok.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.minio.StatObjectResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import naturegecko.jingjok.configurations.EnumConfig.MINIO_DIRECTORY;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class TrackStreamingService {

	private final static int BYTE_RANGE = 128;

	@Autowired
	private MinioStorageService minioStorageService;

	@GetMapping(value = "/music", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<?> getMusic(HttpServletRequest request, HttpServletResponse response) {
		Resource inputStreamResource = new InputStreamResource(minioStorageService
				.trackRetrivelService("testingsite/HEYYEYAAEYAAAEYAEYAA.mp3", "", request, response));
		return ResponseEntity.ok().body(inputStreamResource);
	}

	@GetMapping(value = "/directFromSpringToMin", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@SneakyThrows
	public Resource playAutio(@RequestHeader(value = "Range", required = false) String contentRange,
			@RequestHeader(value = "If-Range", required = false) String ifRange, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("/directFromSpringToMin | size : " + contentRange + " | " + ifRange);
		InputStream getTrack = minioStorageService.trackRetrivelService("testingsite/testmusic112.mp3", contentRange,
				request, response);
		Resource sendThis = new InputStreamResource(getTrack);
		return sendThis;
	}

	@GetMapping(value = "/getContentService")
	@SneakyThrows
	public Mono<ResponseEntity<byte[]>> streamTrackService(
			@RequestHeader(value = "Range", required = false) String range,
			@RequestHeader(value = "If-Range", required = false) String ifRange, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("/directFromSpringToMin | size : " + range + " | " + ifRange);
		return null;
	}

	// Note here

	/*
	 * ------------------------------------------ Direct request header
	 * 
	 * REQUEST Range: bytes=29065216-72517964
	 * 
	 * RESPONSE Content-Length: 20121933 Content-Range: bytes
	 * 52396032-72517964/72517965 ------------------------------------------ Spring
	 * to MINIO
	 * 
	 * REQUEST Range: bytes=131072-
	 * 
	 * RESPONSE Content-Length: 72517965 ------------------------------------------
	 * 
	 */

	@SneakyThrows
	private ResponseEntity<byte[]> getContent(String fileName, String location, HttpServletRequest request,
			HttpServletResponse response) {

		long startRange = 0;
		long endRange;
		long trackSize;
		byte[] trackByteData;
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		///e
		StatObjectResponse statObject = minioStorageService
				.getStatObject(MINIO_DIRECTORY.BUCKET.getDestinationDirectory(), location + fileName);
		trackSize = statObject.size();
		///
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@SneakyThrows
	private byte[] readTrackByteRange(String fileName, String location, long start, long end) {

		InputStream inputStream = minioStorageService.trackRetrivelByByteRangeService(location + fileName, start, end);
		ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();

		byte[] trackByteData = new byte[BYTE_RANGE];
		int nRead;

		while ((nRead = inputStream.read(trackByteData, 0, trackByteData.length)) != -1) {
			bufferedOutputStream.write(trackByteData, 0, nRead);
		}
		bufferedOutputStream.flush();
		byte[] result = new byte[(int) (end - start) + 1];
		System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
		return result;
	}

}
