package naturegecko.jingjok.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import io.minio.MinioClient;
import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;

@Configuration
@PropertySource("application.properties")
public class MinioConfig {

	@Value("${minio.buckek-name}")
	private String bucketName;

	@Value("${minio.accesskey}")
	private String accessKey;

	@Value("${minio.secretkey}")
	private String secretKey;

	@Value("${minio.endpoint}")
	private String endpoint;

	@Bean
	public MinioClient minioClienBuilder() {
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
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INIT_FAILED, "[ FAILED ] " + e.getMessage());
		}
		
	}
}