package com.application.entities.models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "file_link_ref", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class FileLinkRefModel {
	@Id
	@Column(name = "file_id")
	String fileId;

	// The target ref is an ID of an existing tracks or user account records to
	// point who is the owner of this file.
	@Column(name = "target_account_id")
	@Nullable
	@Basic(optional = true)
	int targetAccountId;

	@Column(name = "target_track_id")
	@Nullable
	@Basic(optional = true)
	int targetTrackId;

	@Column(name = "target_playlist_id")
	@Nullable
	@Basic(optional = true)
	int targetPlaylistId;

	@ManyToOne
	@OnDelete(action = OnDeleteAction.NO_ACTION)
	@JoinColumn(name = "type_id", referencedColumnName = "type_id")
	private FileTypeModel fileType;

}
