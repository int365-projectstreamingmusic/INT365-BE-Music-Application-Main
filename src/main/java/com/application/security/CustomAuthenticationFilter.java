package com.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.application.utilities.JwtTokenUtills;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("userName");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authenToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		User userLogin = (User) authentication.getPrincipal();
		String accessToken = JwtTokenUtills.createToken(userLogin);
		String refreshToken = JwtTokenUtills.createRefreshToken(userLogin);

		response.addHeader("accessToken", accessToken);
		response.addHeader("refreshToken", refreshToken);
	}
}
