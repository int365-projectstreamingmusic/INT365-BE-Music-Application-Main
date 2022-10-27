package com.application.entities.submittionforms;

import javax.persistence.Basic;

import org.springframework.data.domain.Page;

import com.application.entities.models.AlbumModel;
import com.application.entities.models.TracksModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumOutput {

	@Basic(optional = true)
	private AlbumModel album;
	@Basic(optional = true)
	private Page<TracksModel> tracks;

}
