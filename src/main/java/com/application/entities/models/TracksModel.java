package com.application.entities.models;

import java.util.List;

import javax.persistence.Basic;
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
@Table(name = "tracks", schema = "sitgarden")
public class TracksModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "track_id")
	private int id;

	@Column(name = "track_file")
	private String trackFile;

	@Column(name = "track_name")
	private String trackName;

	@Column(name = "track_desc")
	private String trackDesc;

	@JsonFormat(pattern = "yyyy/mm/dd")
	private String timestamp;

	@Column(name = "view_count")
	private int viewCount;

	@Column(name = "favorite_count")
	private int favoriteCount;

	@Column(name = "duration")
	private int duration;

	@Column(name = "track_thumbnail")
	@Basic(optional = true)
	private String trackThumbnail;

	@ManyToOne
	@JoinColumn(name = "status_id", referencedColumnName = "status_id")
	private PlayTrackStatusModel playTrackStatus;

	@ManyToOne
	@JoinColumn(name = "album_id", referencedColumnName = "album_id")
	private AlbumModel albums;

	@OneToMany
	@JoinColumn(name = "track_id", referencedColumnName = "track_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<GenresTracksModel> genreTrack;

	@OneToMany
	@JoinColumn(name = "track_id", referencedColumnName = "track_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<MoodTrackModel> moods;

	@OneToMany
	@JoinColumn(name = "track_id", referencedColumnName = "track_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<ArtistsTrackModel> artistTracks;

	/*@JsonIgnore
	@Basic(optional = false)
	@Column(name = "account_id")
	private int accountId;*/
	
	@ManyToOne
	@JoinColumn(name = "account_id",referencedColumnName = "account_id")
	private UserAccountModel owner;
	
	@Transient
	private List<CommentTrackModel> comments;

	@Transient
	private boolean isFavorite;
	@Transient
	private boolean isPlayground;
	@Transient
	private boolean isInHistory;

}
