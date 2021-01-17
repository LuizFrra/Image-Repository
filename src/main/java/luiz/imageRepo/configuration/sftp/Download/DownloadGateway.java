package luiz.imageRepo.configuration.sftp.Download;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.InputStream;


@MessagingGateway
public interface DownloadGateway {

    @Gateway(requestChannel = "downloadFile")
    InputStream downloadFile(String fileName);

}

