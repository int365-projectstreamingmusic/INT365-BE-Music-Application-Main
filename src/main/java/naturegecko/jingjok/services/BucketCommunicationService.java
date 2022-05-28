package naturegecko.jingjok.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
import naturegecko.jingjok.configurations.MinioConfig;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;

@Service
public class BucketCommunicationService {

	private static MinioClient MINIO_CLIENT;
	private static MinioConfig MINIO_CONFIG;

	@Value("${minio.buckek-name}")
	private String bucketname;

	@Value("${minio.maximumfilesize}")
	private long maximumfilesize;

	public BucketCommunicationService(MinioClient minioClient, MinioConfig minioConfig) {
		BucketCommunicationService.MINIO_CLIENT = minioClient;
		BucketCommunicationService.MINIO_CONFIG = minioConfig;
	}

	public StatObjectResponse getStatObject(String targetObject) {
		return null;
	}

	// Return "True" if found and will return false if that bucket is not exist, or
	// return error when an error occured at MinIO server.
	public static boolean pingBucket(String bucketName) {
		try {
			return MINIO_CLIENT.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.MINIO_BUCKER_UNREACHABLE,
					"[ Error ] | " + exc.getMessage());
		}
	}

}
