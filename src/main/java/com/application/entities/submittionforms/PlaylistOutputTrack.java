package com.application.entities.submittionforms;

import javax.persistence.Basic;

import com.application.entities.models.PlayTrackStatusModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistOutputTrack {

	@Basic(optional = true)
	private int id;
	@Basic(optional = true)
	private String trackName;
	@Basic(optional = true)
	private String trackFile;
	@Basic(optional = true)
	private boolean isFavorite;
	@Basic(optional = true)
	private boolean isPlayground;
	@Basic(optional = true)
	private String trackThumbnail;
	@Basic(optional = true)
	private PlayTrackStatusModel status;

}
