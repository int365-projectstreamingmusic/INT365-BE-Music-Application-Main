package naturegecko.jingjok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naturegecko.jingjok.models.entities.UserAccountModel;

@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccountModel, Integer> {

}
