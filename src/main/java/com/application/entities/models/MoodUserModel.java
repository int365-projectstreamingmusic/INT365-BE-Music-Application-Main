package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.application.entities.copmskeys.MoodUserCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mood_user", schema = "sitgarden")
public class MoodUserModel {

	@EmbeddedId
	private MoodUserCompKey id;

	@Column(name = "ratio")
	private int ratio;

	@ManyToOne
	@JoinColumn(name = "mood_id", referencedColumnName = "id", insertable = false, updatable = false)
	private MoodModel mood;

}