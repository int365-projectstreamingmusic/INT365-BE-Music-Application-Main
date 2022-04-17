package naturegecko.jingjok.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.StatObjectResponse;

@Service
public class FileControllers {
	
	public Resource loadImageFromStorage(String imageName, String destination) {
	
		return null;
	}
	
	
	public String uploadUserProfileImageToStorage(MultipartFile imageFIle, HttpRequest request) {
		return "";
	}
	
	/*
	 * 
	 *  /** *  Get a file object as a stream （ Breakpoint download ） * * @param bucketName  Bucket name  * @param objectName  The name of the object in the bucket  * @param offset  The position of the start byte  * @param length  Length to read  ( Optional , If there is no value, it means reading to the end of the file ) * @return
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            StatObjectResponse statObject = statObject(bucketName, objectName);
            if (statObject != null && statObject.size() > 0) {
                InputStream stream =
                        minioClient.getObject(
                                GetObjectArgs.builder()
                                        .bucket(bucketName)
                                        .object(objectName)
                                        .offset(offset)
                                        .length(length)
                                        .build());
                return stream;
            }
        }
        return null;
    }*/

	
	
	
}
