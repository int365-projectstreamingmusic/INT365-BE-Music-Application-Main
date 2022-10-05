package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistTrackForm {

	@Basic(optional = true)
	private int trackId;
	
	@Basic(optional = true)
	private int artistId;
	
	@Basic(optional = true)
	private String description;
}
