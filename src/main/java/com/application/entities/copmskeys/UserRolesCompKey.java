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
public class UserRolesCompKey implements Serializable {

	@Column(name = "account_id")
	private int account_id;

	@Column(name = "roles_id")
	private int roles_id;
}
