package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.MoodUserCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mood_user", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoodUserModel {

	@EmbeddedId
	private MoodUserCompKey id;

	@Column(name = "ratio")
	private int ratio;

}