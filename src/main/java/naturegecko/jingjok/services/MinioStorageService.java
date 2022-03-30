package naturegecko.jingjok.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import lombok.SneakyThrows;
import naturegecko.jingjok.configurations.MinioConfig;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;
import naturegecko.jingjok.utilities.NameGeneratorUtill;

@Service
public class MinioStorageService {

	private static final String EXTENTION_IMAGE = ".jpg";
	private static final String EXTENTION_TRACKS = ".mp3";

	private final MinioClient minioClient;
	private final MinioConfig minioConfig;

	@Value("${minio.buckek-name}")
	private String bucketname;

	@Value("${minio.maximunfilesize}")
	private long maximumFileSize;

	public MinioStorageService(MinioClient minioClient, MinioConfig minioConfig) {
		this.minioClient = minioClient;
		this.minioConfig = minioConfig;
	}

	// Image upload
	public String uploadImageToStorage(InputStream image, String destination) {
		try {
			String imageName = NameGeneratorUtill.generateImageName() + EXTENTION_IMAGE;
			minioClient.putObject(PutObjectArgs.builder().bucket(bucketname).object(destination + imageName)
					.stream(image, -1, maximumFileSize).contentType("image/jpg").build());
			return imageName;
		} catch (Exception ex) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED,
					"[ ERROR ] File save failed with known reason : " + ex.getMessage());
		}
	}

	// Track upload
	public String uploadMusicToStorage(InputStream trackFile, String destination) {
		try {
			String trackName = NameGeneratorUtill.generateTrackName() + EXTENTION_TRACKS;
			minioClient.putObject(PutObjectArgs.builder().bucket(bucketname).object(destination + trackName)
					.stream(trackFile, -1, maximumFileSize).contentType("audio/mpeg").build());
			return trackName;
		} catch (Exception ex) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED,
					"[ ERROR ] File save failed with known reason : " + ex.getMessage());
		}
	}

	public List<String> listAllBuckets() {
		try {
		List<Bucket> bucketList = minioClient.listBuckets();
		List<String> bucketNameList = new ArrayList<>();
		for (Bucket bucket : bucketList) {
			bucketNameList.add(bucket.name());
		}
		return bucketNameList;
		}catch (Exception ex) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INIT_FAILED, "[ FAILED ] BUcket might not exist : " + ex.getMessage());
		}
	}

	// Upload to MINIO
	public void uploadToStorage(MultipartFile file, String destination, String fileName) {
		try {
			InputStream inputStream = new ByteArrayInputStream(file.getBytes());
			minioClient.putObject(PutObjectArgs.builder().bucket(this.bucketname).object(destination + fileName)
					.stream(inputStream, -1, maximumFileSize).contentType("image/jpg").build());
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED, e.getMessage());
		}
	}

	// Call by object name
	// It will generate the link to the resource.
	@SneakyThrows
	public String getObjectAccessUrl(String directory, String fileName) {
		// String destinationUrl = "";
		String destinationUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.GET)
				.bucket("naturegeckogroup").object(directory + fileName).expiry(60, TimeUnit.SECONDS).build());
		return destinationUrl;
	}

}
