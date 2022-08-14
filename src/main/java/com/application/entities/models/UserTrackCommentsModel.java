package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_track_comments", schema = "sitgarden")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackCommentsModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int playlist_comment_id;

	@JsonFormat(pattern = "yyyy/mm/dd")
	private String timestamp;

	private String comment;
	private int account_id;
	private int track_id;

}
