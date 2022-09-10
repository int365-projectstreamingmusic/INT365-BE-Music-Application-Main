package com.application.entities.models;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "playlist", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlaylistModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int playlist_id;

	private String playlist_name;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private String created_date;

	private String playlist_desc;

	private String thumbnail;

	@ManyToOne
	@JoinColumn(name = "account_id", updatable = false, insertable = false)
	private UserAccountModel userAccountModel;
	
	@OneToMany
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "target_playlist_id")
	@Basic(optional = true)
	private List<FileLinkRefModel> fileLinkRef;

}
