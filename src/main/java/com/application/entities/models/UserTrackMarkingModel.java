package com.application.entities.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.application.entities.copmskeys.UserTrackMarkingCompkey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_track_marking", schema = "sitgarden")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserTrackMarkingModel {

	@EmbeddedId
	private UserTrackMarkingCompkey id;

}
