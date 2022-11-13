package com.application.entities.submittionforms;

import com.application.entities.models.UserAccountModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionForm {

	private UserAccountModel user;
	private int targetRef;
	private int actionTypeId;
	private String description;

}
