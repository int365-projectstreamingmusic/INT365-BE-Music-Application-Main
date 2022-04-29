package naturegecko.jingjok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import naturegecko.jingjok.models.entities.RolesModel;

@Repository
public interface RolesRepository extends JpaRepository<RolesModel, Integer> {

}
