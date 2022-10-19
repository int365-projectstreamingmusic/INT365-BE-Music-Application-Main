package com.application.entities.submittionforms;

import java.util.List;

import javax.persistence.Basic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistForm {

	@Basic(optional = true)
	private int id;
	@Basic(optional = true)
	private String playlistName;
	@Basic(optional = true)
	private String playlistDesc;
	@Basic(optional = true)
	List<TrackListSub> trackLst;

}