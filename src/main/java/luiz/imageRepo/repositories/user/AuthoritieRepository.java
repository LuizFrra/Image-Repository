package luiz.imageRepo.repositories.user;

import luiz.imageRepo.entities.user.Authoritie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthoritieRepository extends CrudRepository<Authoritie, Long> {
}
