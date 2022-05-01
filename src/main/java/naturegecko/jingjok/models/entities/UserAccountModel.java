package naturegecko.jingjok.models.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_accounts", schema = "playmylist")
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountModel {

	@Id
	private int account_id;

	private String username;
	private String email;

	private String first_name;
	private String last_name;
	private String user_bios;

	@JsonIgnore
	private String user_passcode;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private List<UserRoleModel> userRoles;

	// @OneToMany(mappedBy = "user_roles", orphanRemoval = true,fetch =
	// FetchType.LAZY)
	// private List<UserRoleModel> userRoleModel;

}
