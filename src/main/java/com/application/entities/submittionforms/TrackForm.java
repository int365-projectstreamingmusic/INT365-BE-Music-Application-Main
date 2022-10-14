package com.application.entities.submittionforms;

import java.util.List;

import javax.persistence.Basic;

import com.application.entities.models.GenreModel;
import com.application.entities.models.PlayTrackStatusModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackForm {

	@Basic(optional = true)
	private int id;

	@Basic(optional = true)
	private String trackName;
	@Basic(optional = true)
	private String trackDesc;

	@Basic(optional = true)
	private List<GenreModel> genreList;
	@Basic(optional = true)
	private PlayTrackStatusModel status;
}
