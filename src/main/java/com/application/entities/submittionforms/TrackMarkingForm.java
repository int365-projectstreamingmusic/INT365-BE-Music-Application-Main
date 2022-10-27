package com.application.entities.submittionforms;

import javax.persistence.Basic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackMarkingForm {

	@Basic(optional = true)
	private int[] trackId;

}
