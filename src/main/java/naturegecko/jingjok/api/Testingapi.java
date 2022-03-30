package naturegecko.jingjok.api;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("")
@AllArgsConstructor
public class Testingapi {

	@Autowired
	private final MinioStorageService minioUtil;

	@GetMapping("")
	public String test() {
		return NameGeneratorUtill.generateImageName() + "\n\n\n\n" + NameGeneratorUtill.generateUserUUID();
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
		if(!FIleExtentionCheckerUtill.fileMatchValidImage(file)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID, "[ REJECTED ] Only accept PNG , GIF , and JPG");
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
