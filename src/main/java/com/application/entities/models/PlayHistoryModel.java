package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "play_history", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayHistoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int history_id;

	@JsonFormat(pattern = "yyyy-mm-dd")
	private String timestamp;

	private int track_id;
	
	@Column(name = "account_id")
	private int accountid;
	
	@ManyToOne
	@JoinColumn(name = "track_id", referencedColumnName = "track_id",insertable = false,updatable = false)
	private TracksModel track;

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
	private UserAccountModel userAccount;*/
}
