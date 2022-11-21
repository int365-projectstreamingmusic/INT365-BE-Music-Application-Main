package com.application.entities.submittionforms;

import javax.persistence.Basic;

import org.springframework.data.domain.Page;

import com.application.entities.models.CommentPlaylistModel;
import com.application.entities.models.CommentTrackModel;
import com.application.entities.models.ReportGroupModel;
import com.application.entities.models.TracksModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOutput {

	private int numberOfReport;
	private int hoursAfterFirstReport;
	private String firstReportDate;

	//private Page<ReportGroupModel> reportGroup;

	@Basic(optional = true)
	private TracksModel track;
	@Basic(optional = true)
	private CommentTrackModel trackComment;
	@Basic(optional = true)
	private CommentPlaylistModel playlistComment;
	@Basic(optional = true)
	private String note;

}
