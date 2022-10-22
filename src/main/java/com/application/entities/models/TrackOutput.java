package com.application.entities.models;

import javax.persistence.Basic;

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
