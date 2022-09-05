package com.application.entities.copmskeys;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountCompKey implements Serializable {

	@Column(name = "track_id")
	private int trackId;

	@Column(name = "view_count_date")
	@JsonFormat(pattern = "yyyy/mm/dd")
	private String countDate;

}
