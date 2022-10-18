package com.application.entities.submittionforms;

import javax.persistence.Basic;

import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import com.application.entities.models.PlaylistModel;
import com.application.entities.models.TracksModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistOutput {

	@Basic(optional = true)
	private PlaylistModel playlist;

	@Basic(optional = true)
	@Nullable
	private Page<TracksModel> tracks;

}
