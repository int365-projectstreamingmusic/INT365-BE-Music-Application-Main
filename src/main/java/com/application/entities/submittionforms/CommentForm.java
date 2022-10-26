package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentForm {

	@Basic(optional = true)
	private int playlistId;
	@Basic(optional = true)
	private int trackId;

	@Basic(optional = true)
	private String comment;
	@Basic(optional = true)
	private int id;

}
