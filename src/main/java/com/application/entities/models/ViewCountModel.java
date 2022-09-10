package com.application.entities.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.application.entities.copmskeys.ViewCountCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "view_count", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class ViewCountModel {

	@Id
	private ViewCountCompKey id;

	@Column(name = "view_count")
	private int viewCount;

}
