package com.application.entities.submittionforms;

import java.util.List;

import javax.persistence.Basic;

import com.application.entities.models.GenreModel;
import com.application.entities.models.GenresTracksModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddNewTrackForm {

	@Basic(optional = true)
	private String trackName;
	@Basic(optional = true)
	private String trackDesc;

	@Basic(optional = true)
	private List<GenreModel> genreList;
}
