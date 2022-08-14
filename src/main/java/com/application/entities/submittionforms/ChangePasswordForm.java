package com.application.entities.submittionforms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordForm {

	private String oldPassword;
	private String newPassword;
	private String confirmationPassword;
}
