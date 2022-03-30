package naturegecko.jingjok.utilities;

import java.time.LocalDate;
import java.util.Random;

public class NameGeneratorUtill {

	private static final int firstAscii = 65;
	private static final int lastAscii = 122;
	// The number 65 represents "A" and 122 for "z" ,
	// ignore 91 - 96 because they are not a letter.

	// Generate name for tracks and musix.
	public static String generateTrackName() {
		return "Track-" + dateTimeGatherer() + "-" + generateString(12);
	}

	// Generate name for all image uploaded to the storage.
	public static String generateImageName() {
		return "Img-" + dateTimeGatherer() + "-" + generateString(15);
	}

	// Generate user unique ID to be used as primary key.
	public static String generateUserUUID() {
		return dateTimeGatherer() + generateString(18);
	}

	private static String generateString(int textLength) {
		Random randomString = new Random();
		StringBuilder buffer = new StringBuilder(textLength);

		for (int i = 0; i < textLength; i++) {
			int randomizedInt = firstAscii + (int) (randomString.nextFloat() * (lastAscii - firstAscii + 1));
			if (randomizedInt >= 91 && randomizedInt <= 96) {
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
