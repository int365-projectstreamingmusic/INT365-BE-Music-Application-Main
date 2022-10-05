package com.application.utilities;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.application.entities.models.UserAccountModel;
import com.application.exceptons.ExceptionFoundation;
import com.application.exceptons.ExceptionResponseModel.EXCEPTION_CODES;
import com.application.repositories.UserAccountRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Service
public class JwtTokenUtills {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private static String secret = "A0fzwr2zfklJ1nnapakMyL0ve1y2001dfgry4xcf5rycf";
	private static int expireInMili = 43200000;
	private static String issuedBy = "KakioStaffAtThanaphat";

	// Only use user info
	public static String createToken(User userLogin) {
		String accessToken = JWT.create().withSubject(userLogin.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + expireInMili)).withIssuer(issuedBy)
				.withClaim("roles", userLogin.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
				.sign(getAlgorithm());
		return accessToken;
	}

	// With user info and roles.
	public static String createToken(String username, String[] roles) {
		String accessToken = JWT.create().withSubject(username)
				.withExpiresAt(new Date(System.currentTimeMillis() + expireInMili)).withIssuer(issuedBy)
				.withArrayClaim("roles", roles).sign(getAlgorithm());
		return accessToken;
	}

	// Create refresh token when an old token is about ti expire.
	public static String createRefreshToken(User userLogin) {
		String accessToken = JWT.create().withSubject(userLogin.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + expireInMili + 60000)).withIssuer(issuedBy)
				.sign(getAlgorithm());
		return accessToken;
	}

	public static Algorithm getAlgorithm() {
		return Algorithm.HMAC256(secret.getBytes());
	}

	// GetUsernameFromToken
	public static String getUserNameFromToken(HttpServletRequest request) {
		String userName;
		String authenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authenHeader != null && authenHeader.startsWith("Bearer ")) {
			String token = authenHeader.substring("Bearer ".length());
			JWTVerifier verifier = JWT.require(getAlgorithm()).build();
			DecodedJWT decodedJWT = verifier.verify(token);
			userName = decodedJWT.getSubject();
			return userName;
		} else {
			throw new ExceptionFoundation(EXCEPTION_CODES.AUTHEN_EMAIL_ALREADY_EXIST, HttpStatus.UNAUTHORIZED,
					authenHeader);
		}
	}

	// GetUserAvvountFromToken
	public UserAccountModel getUserAccountFromToken(HttpServletRequest request) {
		String targetUserName = JwtTokenUtills.getUserNameFromToken(request);
		UserAccountModel targetUser = userAccountRepository.findByUsername(targetUserName);
		if (targetUser == null) {
			throw new ExceptionFoundation(EXCEPTION_CODES.USER_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND,
					"[ EXCEPTION ] The user with this name does not exist.");
		}
		return targetUser;
	}

}
