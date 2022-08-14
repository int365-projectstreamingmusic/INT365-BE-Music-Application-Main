package com.application.entities.submittionforms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegiserationForm {

	private String username;
	private String email;
	private String first_name;
	private String last_name;
	private String user_passcode;

}
