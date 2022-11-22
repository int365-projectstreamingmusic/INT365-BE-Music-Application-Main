package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_track_comments", schema = "sitgarden")
public class CommentTrackModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "track_comment_id")
	private int id;

	@Column(name = "timestamp")
	private String timestamp;

	@Column(name = "comment")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private UserAccountModel user;

	@ManyToOne
	@JoinColumn(name = "track_id", referencedColumnName = "track_id")
	private TracksModel track;

}
