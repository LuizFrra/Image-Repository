package luiz.imageRepo.configuration.sftp.Upload;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;

@MessagingGateway
public interface UploadGateway {

    @Gateway(requestChannel = "uploadFile")
    void uploadFile(File file);
}


