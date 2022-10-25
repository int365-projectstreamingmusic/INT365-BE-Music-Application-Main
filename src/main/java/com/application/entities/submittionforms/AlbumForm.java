package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumForm {
	
	@Basic(optional = true)
	private int id;
	@Basic(optional = true)
	private String name;
	@Basic(optional = true)
	private String desc;
	@Basic(optional = true)
	private int statusId;
	
}
