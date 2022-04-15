package naturegecko.jingjok.api;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import naturegecko.jingjok.configurations.EnumConfig;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;
import naturegecko.jingjok.services.MinioStorageService;
import naturegecko.jingjok.utilities.FIleExtentionCheckerUtill;
import naturegecko.jingjok.utilities.ImageCompressionUtill;
import naturegecko.jingjok.utilities.NameGeneratorUtill;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class Testingapi {

	@Autowired
	private final MinioStorageService minioUtil;

	@GetMapping(value = "/testtest", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@SneakyThrows
	public Resource playAutio(@RequestHeader(value = "Range", required = false) String contentRange,
			@RequestHeader(value = "If-Range", required = false) String ifRange, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("/directFromSpringToMin | size : " + contentRange + " | " + ifRange);
		InputStream getTrack = minioUtil.trackRetrivelByByteRangeService("testingsite/testmusic112.mp3", 0,
				800000);
		Resource sendThis = new InputStreamResource(getTrack);
		return sendThis;
	}

	@GetMapping("/ctt")
	public String testGetpartialContent(@RequestHeader(value = "Content-Range", required = false) String contentRange) {
		// bytes 15794176-72517964/72517965
		return "The พ้าว : " + contentRange;
	}

	@GetMapping("")
	public String test() {
		return NameGeneratorUtill.generateImageName();
	}

	@PostMapping("")
	public String tetserser(MultipartFile file) {
		return file.getContentType();
	}

	@GetMapping("/bucket")
	public ResponseEntity<List<String>> listAllBucket() {
		try {
			return ResponseEntity.ok().body(minioUtil.listAllBuckets());
		} catch (Exception exc) {
			exc.getCause();
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_CAN_NOT_READ,
					"[ ERROR ] This MINIO credential is incorrect.");
		}
	}

	@PostMapping("/u")
	@SneakyThrows
	public ResponseEntity<String> uploadBufferToMinio(MultipartFile file) {
		if (!FIleExtentionCheckerUtill.fileMatchValidImage(file)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID,
					"[ REJECTED ] Only accept PNG , GIF , and JPG");
		}
		InputStream imgg = ImageCompressionUtill.imageResize(file,
				EnumConfig.IMAGE_PROFILE_CODE.USER_PROFILE.getProfileWidth());
		minioUtil.uploadImageToStorage(imgg, EnumConfig.MINIO_DIRECTORY.USER_PROFILES.getDestinationDirectory());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/u").toString());
		return ResponseEntity.created(uri).body("OK");
	}

	@GetMapping("/im")
	public ResponseEntity<String> getImageByName() {
		return ResponseEntity.ok().body(minioUtil.getObjectAccessUrl("directory/", "music-20220319-jEwAjWUtzkMC.jpg"));
	}

}
