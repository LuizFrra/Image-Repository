package luiz.imageRepo.services.image;

import luiz.imageRepo.entities.image.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface ImageStorageService {

    void init();

    void save(MultipartFile file);

    void save(MultipartFile file, String fileName);

    Resource load(String filename) throws MalformedURLException;

    void deleteAll();

    void saveOnFTP(Image image);

}
