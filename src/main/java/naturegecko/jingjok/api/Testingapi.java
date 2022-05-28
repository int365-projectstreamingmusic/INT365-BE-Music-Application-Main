package naturegecko.jingjok.api;

import java.io.InputStream;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import naturegecko.jingjok.configurations.EnumConfig;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;
import naturegecko.jingjok.models.entities.RolesModel;
import naturegecko.jingjok.models.entities.TracksModel;
import naturegecko.jingjok.models.entities.UserAccountModel;
import naturegecko.jingjok.models.entities.UserRoleModel;
import naturegecko.jingjok.models.entities.compkeys.UserRolesID;
import naturegecko.jingjok.repositories.RolesRepository;
import naturegecko.jingjok.repositories.TracksRepository;
import naturegecko.jingjok.repositories.UserAccountsRepository;
import naturegecko.jingjok.repositories.UserRolesRepository;
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
	@Autowired
	private RolesRepository roleRepository;
	@Autowired
	private UserAccountsRepository userAccountsRepository;
	@Autowired
	private UserRolesRepository userRoleRepository;
	@Autowired
	private TracksRepository tracksRepository;

	@PostMapping(value = "/tracks/new", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_OCTET_STREAM_VALUE })
	public ResponseEntity<TracksModel> addTrack(@RequestPart("newTrack") TracksModel newTrack,
			@RequestPart("tracks") MultipartFile trackFile) {
		String autoTrackName = "";
		String originamFileName = trackFile.getOriginalFilename();

		if (trackFile.getOriginalFilename().length() > 43) {
			autoTrackName = trackFile.getOriginalFilename().substring(0, 43);
		} else {
			autoTrackName = trackFile.getOriginalFilename();
		}

		TracksModel createNewTrack = new TracksModel();
		createNewTrack.setTrack_name(autoTrackName);
		createNewTrack.setTimestamp(Timestamp.from(Instant.now()));
		try {
			createNewTrack.setTrack_file(minioUtil.uploadMusicToStorage(trackFile, "/tracks/musics/")
					+ originamFileName.substring(originamFileName.lastIndexOf(".")));
			createNewTrack.setDuration(9999);
			createNewTrack.setAccount_id(1);
			createNewTrack.setTrack_desc(trackFile.getOriginalFilename() + " | " + trackFile.getSize());
			TracksModel resultTrack = tracksRepository.save(createNewTrack);
			return ResponseEntity.ok().body(resultTrack);
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED, "AHHHHHHHH!!!!!");
		}

	}

	@GetMapping("/tracks")
	public ResponseEntity<List<TracksModel>> getAllTracks() {
		List<TracksModel> result = tracksRepository.findAll();
		if (result.size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, "There is nothing in this list");
		}

		return ResponseEntity.ok().body(tracksRepository.findAll());
	}

	@GetMapping("/uuidplease")
	public ResponseEntity<String> generateTrackUUID() {
		return ResponseEntity.ok().body(NameGeneratorUtill.generateTrackNameUUID());
	}

	@GetMapping("/roles")
	public ResponseEntity<HashMap<String, Object>> getAllROles(HttpServletResponse response) {
		// List<UserRoleModel> resultList = userRoleRepository.findAll();

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("resoinseObject", roleRepository.findAll());
		resultMap.put("Method", "/roles");

		return ResponseEntity.ok().body(resultMap);
	}

	@DeleteMapping("/nukeUser/{userId}")
	public ResponseEntity<String> nukeUser(@PathVariable int userId) {
		try {
			userAccountsRepository.deleteById(userId);
			return ResponseEntity.ok().body("NUKED | ID: " + userId);
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_METHOD_FAILED, "Died");
		}
	}

	@GetMapping("/getUser/{userId}")
	public ResponseEntity<UserAccountModel> getUser(@PathVariable int userId) {

		UserAccountModel newMode = userAccountsRepository.findById(userId).orElseThrow(
				() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND, "NOPE | Don't know this person. :V"));

		// userAccountsRepository.deleteById(1);
		return ResponseEntity.ok().body(newMode);
	}

	@PutMapping("/addRole/{userId}/{role}")
	public ResponseEntity<UserAccountModel> addRole(@PathVariable("userId") int user,
			@PathVariable("role") String role) {

		int userId = user;
		int roleId = Integer.parseInt(role);
		System.out.println(userId + " | " + roleId);

		UserAccountModel currentUs = userAccountsRepository.findById(userId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
						"NOPE | User with id " + user + " is not exist."));
		;
		List<UserRoleModel> roleList = currentUs.getUserRoles();

		UserRoleModel newRole = new UserRoleModel();
		newRole.setRoles(roleRepository.findById(roleId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
						"NOPE | User with id " + user + " is not exist.")));

		UserRolesID currentId = new UserRolesID(userId, roleId);
		if (userRoleRepository.existsById(currentId)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED, "This user already has this role.");
		}

		newRole.setUserRoleId(currentId);

		roleList.add(newRole);
		currentUs.setUserRoles(roleList);

		userAccountsRepository.save(currentUs);

		return ResponseEntity.ok().body(currentUs);
	}

	@PutMapping("/deRole/{userId}/{role}")
	public ResponseEntity<UserAccountModel> deRole(@PathVariable("userId") int user,
			@PathVariable("role") String role) {

		int userId = user;
		int roleId = Integer.parseInt(role);

		UserAccountModel currentUs = userAccountsRepository.findById(userId)
				.orElseThrow(() -> new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
						"NOPE | User with id " + user + " is not exist."));

		List<UserRoleModel> roleList = currentUs.getUserRoles();

		UserRolesID getId = new UserRolesID(userId, roleId);
		try {
			userRoleRepository.deleteById(getId);
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SEARCH_NOT_FOUND,
					"NOPE | Role with id " + role + " is not exist.");
		}

		for (int i = 0; i < roleList.size(); i++) {
			System.out.println(roleList.get(i).getRoles().getRoles_id());
		}
		return ResponseEntity.ok().body(currentUs);
	}

	@GetMapping("/userroles")
	public ResponseEntity<List<UserRoleModel>> getAllROlesss() {
		return ResponseEntity.ok().body(userRoleRepository.findAll());
	}

	@GetMapping("/accounts")
	public ResponseEntity<List<UserAccountModel>> getAllAccount() {
		return ResponseEntity.ok().body(userAccountsRepository.findAll());
	}

	@GetMapping("/first")
	public ResponseEntity<Map<String, String>> testNameGeneration() {
		String trackName = NameGeneratorUtill.generateTrackNameUUID();
		String userName = NameGeneratorUtill.generateUserUUID();
		String playlistUUID = NameGeneratorUtill.generatePlaylistUUID();
		String imageName = NameGeneratorUtill.generateImageNameUUID();

		Map<String, String> result = new HashMap<String, String>();
		result.put("Method", "testNameGeneration");
		result.put("Track name generation", trackName + " | " + trackName.length());
		result.put("UserID generation", userName + " | " + userName.length());
		result.put("playlist generation", playlistUUID + " | " + playlistUUID.length());
		result.put("image generation", imageName + " | " + imageName.length());

		return ResponseEntity.ok().body(result);
	}

	@GetMapping(value = "/testtest", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@SneakyThrows
	public Resource playAutio(@RequestHeader(value = "Range", required = false) String contentRange,
			@RequestHeader(value = "If-Range", required = false) String ifRange, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("/directFromSpringToMin | size : " + contentRange + " | " + ifRange);
		InputStream getTrack = minioUtil.trackRetrivelByByteRangeService("testingsite/testmusic112.mp3", 0, 800000);
		Resource sendThis = new InputStreamResource(getTrack);
		return sendThis;
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

	@PostMapping("/uploadeProfile")
	@SneakyThrows
	public ResponseEntity<String> uploadBufferToMinio(MultipartFile file) {
		if (!FIleExtentionCheckerUtill.fileMatchValidImage(file)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID,
					"[ REJECTED ] Only accept PNG , GIF , and JPG");
		}
		InputStream imgg = ImageCompressionUtill.imageResize(file,
				EnumConfig.IMAGE_PROFILE_CODE.USER_PROFILE.getProfileWidth());
		minioUtil.uploadImageToStorage(imgg, EnumConfig.MINIO_DIRECTORY.USER_PROFILES.getDestinationDirectory());
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/uploadeProfile").toString());
		return ResponseEntity.created(uri).body("OK");
	}

	@GetMapping("/im")
	public ResponseEntity<String> getImageByName() {
		return ResponseEntity.ok().body(minioUtil.getObjectAccessUrl("directory/", "music-20220319-jEwAjWUtzkMC.jpg"));
	}

}
