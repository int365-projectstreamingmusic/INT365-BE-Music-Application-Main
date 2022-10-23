package com.application.entities.submittionforms;

import java.util.List;
import javax.persistence.Basic;

import com.application.entities.models.GenreModel;
import com.application.entities.models.MoodModel;
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
	private int statusId;
	@Basic(optional = true)
	private boolean autoAddMusic;

	@Basic(optional = true)
	private List<TrackListSub> trackLst;
	@Basic(optional = true)
	private List<MoodModel> moods;
	@Basic(optional = true)
	private List<GenreModel> genres;

}
