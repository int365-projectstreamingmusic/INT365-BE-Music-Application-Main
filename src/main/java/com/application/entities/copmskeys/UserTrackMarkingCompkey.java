package com.application.entities.copmskeys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTrackMarkingCompkey implements Serializable {

	@Column(name = "track_id")
	private int track_id;

	@Column(name = "account_id")
	private int account_id;

	@Column(name = "track_marking_id")
	private int track_marking_id;
}
