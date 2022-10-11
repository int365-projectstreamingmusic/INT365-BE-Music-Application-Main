package com.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.application.security.CustomAuthenticationFilter;
import com.application.security.CustomAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().antMatchers(HttpMethod.GET, "api/public/**");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager());
		authenticationFilter.setFilterProcessesUrl("/api/public/login");

		http.cors();
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/api/authen/**").permitAll();
		http.authorizeRequests().antMatchers("/api/public/**").permitAll();
		http.authorizeRequests().antMatchers("/api/streaming/**").permitAll();
		http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority("user", "artist", "admin", "super user");
		http.authorizeRequests().antMatchers("/api/artist/**").hasAnyAuthority("artist", "admin", "super user");
		http.authorizeRequests().antMatchers("/api/staff/**").hasAnyAuthority("admin", "staff", "super user");
		http.authorizeRequests().antMatchers("/api/manager/**").hasAnyAuthority("admin", "super user");
		http.authorizeRequests().antMatchers("/test/**").hasAnyAuthority("super user");

		http.addFilter(authenticationFilter);
		http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
