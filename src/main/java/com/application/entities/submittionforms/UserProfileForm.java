package com.application.entities.submittionforms;

import javax.persistence.Basic;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileForm {

	@Nullable
	@Basic(optional = true)
	private String firstName;

	@Nullable
	@Basic(optional = true)
	private String lastName;

	@Nullable
	@Basic(optional = true)
	private String userBios;

	@Nullable
	@Basic(optional = true)
	private String profileName;

}
