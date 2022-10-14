package com.application.entities.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.application.entities.copmskeys.AlbumTrackCompKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "track_album", schema = "sitgarden")
@AllArgsConstructor
@NoArgsConstructor
public class AlbumTrackModel {

	@Id
	private AlbumTrackCompKey id;
}
