package naturegecko.jingjok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naturegecko.jingjok.models.entities.UserRoleModel;
import naturegecko.jingjok.models.entities.compkeys.UserRolesID;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoleModel, UserRolesID> {

}
