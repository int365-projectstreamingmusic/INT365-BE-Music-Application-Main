package com.application.entities.submittionforms;

import com.application.entities.models.UserAccountModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActionForm {

	public ActionForm(UserAccountModel user, int targetRef, int actionTypeId, String description) {
		this.user = user;
		this.targetRef = targetRef;
		this.actionTypeId = actionTypeId;
		this.description = description;
	}

	private UserAccountModel user;
	private int targetRef;
	private int actionTypeId;
	private String description;

}
