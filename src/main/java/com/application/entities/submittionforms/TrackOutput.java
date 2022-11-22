package com.application.entities.submittionforms;

import javax.persistence.Basic;

import com.application.entities.models.TracksModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackOutput {

	@Basic(optional = true)
	private TracksModel track;

	@Basic(optional = true)
	private boolean isFavorite;

	@Basic(optional = true)
	private boolean isInPlayground;
	
}
