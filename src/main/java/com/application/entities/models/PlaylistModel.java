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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "playlist", schema = "sitgarden")
public class PlaylistModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playlist_id")
	private int id;

	@Column(name = "playlist_name")
	private String playlistName;

	@Column(name = "playlist_desc")
	private String playlistDesc;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_date")
	private String createdDate;

	private String thumbnail;

	@OneToMany
	@JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id")
	private List<GenrePlaylistModel> genres;

	@OneToMany
	@JoinColumn(name = "playlist_id", referencedColumnName = "playlist_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<MoodPlaylistModel> moods;

	@ManyToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private UserAccountModel userAccountModel;

	@ManyToOne
	@JoinColumn(name = "status_id", referencedColumnName = "status_id")
	private PlayTrackStatusModel playTrackStatus;

	@Transient
	private List<CommentPlaylistModel> comments;
}
