package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackListSub {

	@Basic(optional = true)
	private int trackId;

}
