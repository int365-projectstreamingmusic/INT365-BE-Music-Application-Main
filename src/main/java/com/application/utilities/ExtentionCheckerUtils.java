package com.application.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

@Service
public class ExtentionCheckerUtils {

	public static String checkTrackFileExtention(String contentType) {
		String fileType = contentType;
		switch (fileType) {
		case "audio/mpeg": {
			return fileType;
		}
		case "audio/ogg": {
			return fileType;
		}
		default:
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID, HttpStatus.BAD_REQUEST,
					"[ checkTrackFileExtention ] Invalid file extention. ");
		}
	}

	public static String checkImageFileExtention(String contentType) {
		String fileType = contentType;
		switch (fileType) {
		case "image/jpeg": {
			return fileType;
		}
		case "image/png": {
			return fileType;
		}
		case "image/gif": {
			return fileType;
		}
		default:
			throw new ExceptionFoundation(EXCEPTION_CODES.SAVE_FILE_INVALID, HttpStatus.BAD_REQUEST,
					"[ checkImageFileExtention ] Invalid file extention. ");
		}
	}
}
