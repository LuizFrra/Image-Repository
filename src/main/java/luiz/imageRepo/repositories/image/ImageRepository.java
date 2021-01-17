package luiz.imageRepo.repositories.image;

import luiz.imageRepo.entities.image.Image;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends CrudRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    List<Image> findByOriginalName(String originalName);

    Optional<Image> findByRemoteName(String RemoteName);

    List<Image> findByForSelling(boolean forSelling);

}
