package naturegecko.jingjok.utilities;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import naturegecko.jingjok.exceptions.ExceptionFoundation;
import naturegecko.jingjok.exceptions.ExceptionResponseModel.EXCEPTION_CODES;

@Component
public class FIleExtentionCheckerUtill {
	public static boolean fileMatchValidImage(MultipartFile multipartFile) {
		String fileType = "";
		try {
			fileType = multipartFile.getContentType().toLowerCase();
			switch (fileType) {
			case "image/jpeg":
				return true;
			case "image/png":
				return true;
			case "image/gif":
				return true;
			default:
				return false;
			}

		} catch (Exception ex) {
			throw new ExceptionFoundation(EXCEPTION_CODES.DEAD,
					"[ ERROR ? ] No one knows why yhis error occur...." + ex.getMessage());
		}
	}
}
