package naturegecko.jingjok.models.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles", schema = "playmylist")
@AllArgsConstructor
@NoArgsConstructor
public class RolesModel {

	@Id
	private int roles_id;

	private String roles_name;

	@Nullable
	private String role_desc;
}
