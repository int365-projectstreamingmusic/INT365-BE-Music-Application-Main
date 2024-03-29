package com.application.entities.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_group", schema = "sitgarden")
public class ReportGroupModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "group_name")
	private String title;

	@Column(name = "ref_id")
	private int target;

	@Column(name = "solved_date")
	private String solvedDate;

	@Column(name = "recent_date")
	private String recentDate;

	@Column(name = "is_solved")
	private boolean isSolved;

	@Column(name = "startedBy")
	private int startedBy;

	@ManyToOne
	@JoinColumn(name = "type_id", referencedColumnName = "type_id")
	private ReportTypeModel type;

	@OneToMany
	@JoinColumn(name = "report_group_id", referencedColumnName = "id")
	private List<ReportModel> reports;

	@Transient
	private int numberOfReport;
	@Transient
	private TracksModel track;
	@Transient
	private CommentTrackModel commentTrack;
	@Transient
	private CommentPlaylistModel commentPlaylist;
	@Transient
	private String note;
	@Transient
	private String reportedReason;

}
