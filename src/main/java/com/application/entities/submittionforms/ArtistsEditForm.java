package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistsEditForm {
	
	private int artistId;

	@Basic(optional = true)
	private String artistName;

	@Basic(optional = true)
	private String artistBio;
}
