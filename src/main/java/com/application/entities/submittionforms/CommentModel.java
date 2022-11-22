package com.application.entities.submittionforms;

import com.application.entities.models.TracksModel;
import com.application.entities.models.UserAccountModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {

	private int id;
	private String timestamp;
	private String comment;
	private UserAccountModel user;
	private TracksModel track;

}
