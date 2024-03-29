package com.application.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;

public class StringGenerateService {

	private static final int firstAscii = 48;
	private static final int lastAscii = 122;

	// Generate name for tracks and music.
	public static String generateTrackNameUUID() {
		return "1001-" + dateTimeGatherer() + generateString(12);
	}

	// Generate name for all image uploaded to the storage.
	public static String generateImageNameUUID() {
		return dateTimeGatherer() + generateString(12);
	}

	// Generate user unique ID to be used as primary key.
	public static String generateUserUUID() {
		return generateString(12);
	}

	// Generate Unique ID for playlist.
	public static String generatePlaylistUUID() {
		return generateString(12);
	}

	// Generate Unique ID for track in each playlist.
	public static String generateTrackInPlaylistUUID(String name) {
		return generateString(13);
	}

	// Generate Password Reset Code
	public static String generatePasswordResetCode() {
		return dateTimeGatherer() + generateString(10);
	}

	private static String generateString(int textLength) {
		Random randomString = new Random();
		StringBuilder buffer = new StringBuilder(textLength);

		for (int i = 0; i < textLength; i++) {
			int randomizedInt = firstAscii + (int) (randomString.nextFloat() * (lastAscii - firstAscii + 1));
			if ((randomizedInt >= 91 && randomizedInt <= 96) || (randomizedInt >= 58 && randomizedInt <= 64)) {
				randomizedInt = firstAscii;
			}
			buffer.append((char) randomizedInt);
		}
		return buffer.toString();
	}

	private static String dateTimeGatherer() {
		String currentDate = LocalDate.now().toString();
		return currentDate.substring(0, 4) + currentDate.substring(5, 7) + currentDate.substring(8);
	}

}
