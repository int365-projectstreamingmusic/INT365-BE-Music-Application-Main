package com.application.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

import java.io.*;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.image.*;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;

@Service
public class ImageManagementUtilities {

	public static ByteArrayInputStream imageCompressionService(MultipartFile multipartFile) {
		try {
			byte[] imageBytes = multipartFile.getBytes();
			InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(imageInputStream);

			File compressedImageFile = new File("compress.jpg");
			OutputStream os = new FileOutputStream(compressedImageFile);

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.05f);

			writer.write(null, new IIOImage(image, null, null), param);

			os.close();
			ios.close();
			writer.dispose();

			return null;
		} catch (IOException exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_COMPRESSION_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ imageCompressionService ] Image compression failed. Reason : " + exc.getLocalizedMessage());
		}
	}

	public static void resizeImageService(MultipartFile multipartFile) {
		try {
			byte[] imageBytes = multipartFile.getBytes();
			InputStream imageInputStream = new ByteArrayInputStream(imageBytes);
			BufferedImage image = ImageIO.read(imageInputStream);
			// new BufferedImage(250, 250, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D graphics2d = image.createGraphics();
			
			
			
		} catch (Exception exc) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_COMPRESSION_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ resizeImageService ] Image compression failed. Reason : " + exc.getLocalizedMessage());
		}
	}

}
