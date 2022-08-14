package com.application.utilities;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ValidatorServices {

	Pattern emailPattern = Pattern.compile("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+");
	Pattern passwordPattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,128}$");
	Pattern phoneNumberPattern = Pattern.compile("^[0]\\\\d{1,10}$");
	Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9_-]{6,30}$");

	public boolean validateEmail(String email) {
		return emailPattern.matcher(email).matches();
	}

	public boolean validatePassword(String password) {
		return passwordPattern.matcher(password).matches();
	}

	public boolean validatePhoneNumber(String phoneNumber) {
		return phoneNumberPattern.matcher(phoneNumber).matches();
	}

	public boolean validateUsername(String username) {
		return usernamePattern.matcher(username).matches();
	}
}
