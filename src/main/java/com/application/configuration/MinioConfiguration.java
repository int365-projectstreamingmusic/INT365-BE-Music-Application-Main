package com.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;

import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

import io.minio.MinioClient;

@Configuration
@PropertySource("generalsetting.properties")
public class MinioConfiguration {

	@Value("${minio.bucketname}")
	private String bucketName;

	@Value("${minio.accesskey}")
	private String accessKey;

	@Value("${minio.secretkey}")
	private String secretKey;

	@Value("${minio.endpoint}")
	private String endpoint;

	@Bean
	public MinioClient minioClientBuilder() {
		try {
			MinioClient minioClient = MinioClient.builder().credentials(accessKey, secretKey).endpoint(endpoint)
					.build();
			return minioClient;
		} catch (Exception e) {
			System.out.println("!! ===================================================== !!");
			System.out.println("");
			System.out.println("[ CORE INIT FAILED ]");
			System.out.println("MINIO : Please recheck your access key, secret key, and endpoint, or server status.");
			System.out.println("");
			System.out.println("!! ===================================================== !!");
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_FILE_DUPLICATED, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ minioClientBuilder ] Authentication to MinIO failed. ");
		}
	}

}
