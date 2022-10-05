package com.application.services;

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

	public UserAccountModel checkUserAccountExist(HttpServletRequest request) {
		UserAccountModel userProfile = userAccountRepository
				.findByUsername(JwtTokenUtills.getUserNameFromToken(request));
		if (userProfile == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ USER_ACCOUNT_NOT_FOUND ] This user does not exist in our database.");
		}
		if (userAccountRepository.checkUserAccountIdIfSuspended(userProfile.getAccountId()) != 0) {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_ACCOUNT_SUSPENDED, HttpStatus.FORBIDDEN,
					"[ AUTHEN_ACCOUNT_SUSPENDED ] This account is suspended and is not allowd to commit any action");
		}
		return userProfile;
	}

	public boolean checkOwnerShipForRecord(int userId, int recordUserId) {
		if (userId == recordUserId) {
			return true;
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_NOT_THE_OWNER, HttpStatus.FORBIDDEN,
					"[ AUTHEN_NOT_THE_OWNER ] This user is not the owner of this recode, therefore, no change is mage.");
		}

	}
}
