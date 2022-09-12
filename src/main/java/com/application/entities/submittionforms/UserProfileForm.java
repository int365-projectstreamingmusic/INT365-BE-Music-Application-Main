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
	private String FirstName;

	@Nullable
	@Basic(optional = true)
	private String LastName;

	@Nullable
	@Basic(optional = true)
	private String userBios;

	@Nullable
	@Basic(optional = true)
	private String profileName;

}
