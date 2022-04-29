package naturegecko.jingjok.models.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import naturegecko.jingjok.models.entities.compkeys.UserRolesID;

@SuppressWarnings("serial")
@Data
@Entity
@Table(name = "user_roles", schema = "playmylist")
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleModel implements Serializable {
	@EmbeddedId
	private UserRolesID userRoleId;

	//@ManyToOne
	//@JoinColumn(name = "account_id", insertable = false, updatable = false)
	//private UserAccountModel userAccount;
	
	@ManyToOne
	@JoinColumn(name = "roles_id", insertable = false, updatable = false)
	private RolesModel roles;

	// private int account_id;
	// private int roles_id;

}
