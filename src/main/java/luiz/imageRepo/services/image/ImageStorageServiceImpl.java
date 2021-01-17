package luiz.imageRepo.services.image;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import luiz.imageRepo.configuration.sftp.Upload.UploadGateway;
import luiz.imageRepo.entities.image.Image;
import luiz.imageRepo.entities.image.ImageStatus;
import luiz.imageRepo.utils.LocalPaths;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ImageStorageServiceImpl implements ImageStorageService {

    private final UploadGateway uploadGateway;

    public final ImageDatabaseService imageDatabaseService;

    @PostConstruct
    public void init() {
        try {
            if(Files.notExists(LocalPaths.getUploadFolder()))
                Files.createDirectory(LocalPaths.getUploadFolder());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to create temp upload folder.");
        }
    }

    @Override
    public void save(@NotNull MultipartFile file) {
        try {
            if (file.getOriginalFilename() != null)
                Files.copy(file.getInputStream(), LocalPaths.getUploadFolder().resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to store file Locally.");
        }
    }

    @Override
    public void save(@NotNull MultipartFile file, String fileName) {
        try {
            if (file.getOriginalFilename() != null)
                Files.copy(file.getInputStream(), LocalPaths.getUploadFolder().resolve(fileName));
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new RuntimeException("Unable to store file Locally.");
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = LocalPaths.getUploadFolder().resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable())
                return resource;

            throw new RuntimeException("Can't read file");
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public boolean delete(String fileName) {
        try {
            Files.delete(LocalPaths.getUploadFolder().resolve(fileName));
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(LocalPaths.getUploadFolder().toFile());
        init();
    }

    @Async
    @Override
    public void saveOnFTP(Image image) {
        Path filePath = LocalPaths.getUploadFolder().resolve(image.getRemoteName());
        java.io.File fileLocal = new java.io.File(filePath.toUri());

        if(!fileLocal.exists()) {
            log.info("File {} not found.", filePath);
            return;
        }

        log.info("Starting Upload of file {} to FTP.", fileLocal.getName());

        uploadGateway.uploadFile(fileLocal);

        log.info("Upload done {}", fileLocal.getName());

        Optional<Image> remoteFile = imageDatabaseService.findByRemoteName(image.getRemoteName());

        if(remoteFile.isPresent()) {
            Image imageDb = remoteFile.get();
            imageDb.setStatus(ImageStatus.UPLOADED);
            imageDatabaseService.update(imageDb, imageDb.getUser().getId());
            delete(image.getRemoteName());
        }


        log.info("File {} deleted locally.", image.getRemoteName());
    }

}
