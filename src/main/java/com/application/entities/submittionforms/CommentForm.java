package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentForm {

	@Basic(optional = true)
	private int commentType;
	@Basic(optional = true)
	private String comment;

}
