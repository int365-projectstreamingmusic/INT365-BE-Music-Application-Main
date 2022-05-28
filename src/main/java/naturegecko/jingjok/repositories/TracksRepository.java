package naturegecko.jingjok.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import naturegecko.jingjok.models.entities.TracksModel;

@Repository
public interface TracksRepository extends JpaRepository<TracksModel, Integer> {

}
