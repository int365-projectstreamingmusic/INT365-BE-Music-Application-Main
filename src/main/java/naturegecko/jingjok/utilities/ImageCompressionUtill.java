package naturegecko.jingjok.utilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;

@Component
public class ImageCompressionUtill {

	public static final String EXTENTION = "jpg";

	public static ByteArrayInputStream imageResize(MultipartFile multipartFile, int targetPixel) {
		try {
			byte[] imageBytes = multipartFile.getBytes();
			InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
			BufferedImage imageBuffered = ImageIO.read(imageInputStream);
			BufferedImage resizedImage = Scalr.resize(imageBuffered, targetPixel);

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, EXTENTION, os);
			os.flush();
			return new ByteArrayInputStream(os.toByteArray());
		} catch (Exception e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_COMPRESSION_FAILURE, "Failed " + e.getMessage());
		}
	}

	public static String checkImageWidth(MultipartFile multipartFile) {
		return "1000";
	}
}
