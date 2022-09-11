package com.application.utilities;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.hql.internal.NameGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.configuration.MinioConfiguration;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

import io.minio.BucketExistsArgs;
import io.minio.DeleteObjectTagsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;

@SuppressWarnings("unused")

@Service
@PropertySource("generalsetting.properties")
public class MinioStorageService {

	private static final String EXTENTION_IMAGE = ".jpg";
	private static final String EXTENTION_TRACKS = ".mp3";

	private final MinioClient minioClient;
	private final MinioConfiguration minioConfiguration;

	@Value("${minio.bucketname}")
	private String bucketname;

	@Value("${minio.maximumfilesize}")
	private long maximumFileSize;

	public MinioStorageService(MinioClient minioClient, MinioConfiguration minioConfiguration) {
		this.minioClient = minioClient;
		this.minioConfiguration = minioConfiguration;
	}

	// OK!
	// pingBucket
	public boolean pingBucket(String bucketName) {
		try {
			return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketname).build());
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ pingBucket ] Bucket not found or unreachable.");
		}
	}

	// OK!
	// getStatObjectFromObject
	public StatObjectResponse getStatObjectFromObject(String objectName) {
		if (!pingBucket(objectName)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[  getStatObjectFromObject ] Bucket not found or unreachable.\"");
		}
		StatObjectResponse statObject;
		try {
			statObject = minioClient.statObject(StatObjectArgs.builder().bucket(bucketname).object(objectName).build());
			return statObject;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ getStatObjectFromObject ] The requested object is unreachable." + exc.getLocalizedMessage());
		}
	}

	// OK!
	// GetImageFromMinIoByName
	public Resource getImageFromMinIoByNameLocation(String trackNameLocation) {
		if (!pingBucket(bucketname)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ getImageFromMinIoByNameLocation ] Bucket " + bucketname + " found or unreachable.");
		}

		StatObjectResponse statObject = getStatObjectFromObject(trackNameLocation);
		if (statObject == null || statObject.size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ getImageFromMinIoByNameLocation ] File " + trackNameLocation + " is unreachable.");
		}

		try {
			InputStream stream = minioClient
					.getObject(GetObjectArgs.builder().bucket(bucketname).object(trackNameLocation).build());
			return new InputStreamResource(stream);
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ getImageFromMinIoByNameLocation ] " + exc.getLocalizedMessage());
		}
	}

	// OK!
	// trackRetrivelByByteRangeService
	public InputStream trackRetrivelByByteRangeService(String trackNameAndLocation, long start, long end) {
		if (!pingBucket(bucketname)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ trackRetrivelByByteRangeService ] Bucket " + bucketname + " found or unreachable.");
		}

		StatObjectResponse statObject = getStatObjectFromObject(trackNameAndLocation);
		if (statObject == null || statObject.size() <= 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ trackRetrivelByByteRangeService ] The track name " + trackNameAndLocation + " is unreachable.");
		}

		Map<String, String> requestExtraHeader = new HashMap<String, String>();
		requestExtraHeader.put("Range", "bytes=" + start + "-" + end);

		try {
			InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketname)
					.extraHeaders(requestExtraHeader).object(trackNameAndLocation).build());
			return stream;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ trackRetrivelByByteRangeService ] Method failed because the target file may not exist.");
		}
	}

	// OK!
	// trackUploadToMinIo
	public String uploadTrackToStorage(MultipartFile trackFile, String destination) {
		String fileNameExtention = trackFile.getOriginalFilename()
				.substring(trackFile.getOriginalFilename().lastIndexOf("."));
		if (checkAutioFileExtention(fileNameExtention)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.MINIO_OBJECT_FORMAT_NOT_SUPPORT, HttpStatus.NOT_ACCEPTABLE,
					"[ MinioStorageService ] Can't keep your file with this format. We only accept the MP3 format for now. ");
		}
		try {
			InputStream trackFileStream = new BufferedInputStream(trackFile.getInputStream());
			String trackname = StringGenerateService.generateTrackNameUUID() + EXTENTION_TRACKS;
			minioClient.putObject(PutObjectArgs.builder().bucket(bucketname).object(destination + trackname)
					.stream(trackFileStream, trackFileStream.available(), -1).contentType(trackFile.getContentType())
					.build());
			return trackname;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ uploadTrackToStorage ] Track save failed. Reason : " + exc.getLocalizedMessage());
		}
	}

	// OK!
	// imageUploadToMinIo
	public String uploadImageToStorage(MultipartFile imageFile, String prefix, String imageFileLocation) {
		String imageFileExtention = imageFile.getOriginalFilename()
				.substring(imageFile.getOriginalFilename().lastIndexOf("."));
		if (checkImageFileExtension(imageFileExtention)) {
			throw new ExceptionFoundation(EXCEPTION_CODES.MINIO_OBJECT_FORMAT_NOT_SUPPORT, HttpStatus.NOT_ACCEPTABLE,
					"[ MinioStorageService ] We only accept PNG and JPG.");
		}
		try {
			InputStream imageFileStream = new BufferedInputStream(imageFile.getInputStream());
			String imageFileName = prefix + StringGenerateService.generateImageNameUUID() + imageFileExtention;
			minioClient.putObject(PutObjectArgs.builder().bucket(bucketname).object(imageFileLocation + imageFileName)
					.stream(imageFileStream, imageFileStream.available(), -1).contentType(imageFile.getContentType())
					.build());
			return imageFileName;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_FAILED, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ uploadImageToStorage ] Track save failed. Reason : " + exc.getLocalizedMessage());
		}
	}

	// OK!
	// DeleteObjectFromMinIoByPathAndName
	public String DeleteObjectFromMinIoByPathAndName(String imageFileLocation) {
		StatObjectResponse targetObject = getStatObjectFromObject(imageFileLocation);
		try {
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketname).object(imageFileLocation).build());
			return imageFileLocation;
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ DeleteObjectFromMinIoByName ] Can't delete this object because : " + exc.getLocalizedMessage());
		}
	}

	// checkImageFileExtension
	private boolean checkImageFileExtension(String extension) {
		boolean isAllowed = false;
		switch (extension) {
		case ".png": {
			isAllowed = true;
		}
		case ".jpg": {
			isAllowed = true;
		}
		default:
			isAllowed = false;
		}
		return isAllowed;
	}

	// checkAutioFileExtention
	private boolean checkAutioFileExtention(String extension) {
		boolean isAllowed = false;
		switch (extension) {
		case ".mp3": {
			isAllowed = true;
		}
		default:
			isAllowed = false;
		}
		return isAllowed;
	}

}
