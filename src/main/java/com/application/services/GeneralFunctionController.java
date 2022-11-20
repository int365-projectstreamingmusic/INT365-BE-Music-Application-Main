package com.application.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.UserAccountRepository;
import com.application.utilities.JwtTokenUtills;

@Service
public class GeneralFunctionController {

	@Autowired
	private UserAccountRepository userAccountRepository;

	public final long TIME_DIF_DAY = 86400000;
	public final long TIME_DIF = 25200000;

	// ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬
	// DB-V6 OK!
	// Get user role from Token
	// EXCEPTION | USER_ACCOUNT_NOT_FOUND
	public UserAccountModel getUserAccount(HttpServletRequest request) {
		UserAccountModel userProfile = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		if (userProfile == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ USER_ACCOUNT_NOT_FOUND ] This user does not exist in our database.");
		}
		return userProfile;
	}

	public boolean checkOwnerShipForRecord(int userId, int ownerInRecord) {
		if (userId == ownerInRecord) {
			return true;
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.FORBIDDEN,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not the owner of this recode, therefore, no change is mage.");
		}

	}

	// GetTimestampToday
	public Timestamp getTimestampToday() {
		long currentTimeMili = Calendar.getInstance().getTimeInMillis();
		long currentTimeRecord = currentTimeMili - (currentTimeMili % TIME_DIF_DAY) - TIME_DIF;
		Timestamp timeStamp = new Timestamp(currentTimeRecord);
		return timeStamp;
	}

	// GetTimeStampFromMilisecond
	public Timestamp getTimeStampFromMilisecond(long miliSecond) {
		Timestamp timestamp = new Timestamp(miliSecond);
		return timestamp;
	}

	public long getTimeStampFromString(String myDate) {
		try {
			System.out.println(myDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(myDate);
			System.out.println(date.toString());
			return date.getTime();
		} catch (ParseException e) {
			throw new ExceptionFoundation(EXCEPTION_CODES.CORE_INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR,
					"[ CORE_INTERNAL_SERVER_ERROR ] The parsing of this date gone mulfunction.");
		}
	}
}
