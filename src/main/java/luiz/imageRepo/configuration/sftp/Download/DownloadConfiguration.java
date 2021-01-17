package luiz.imageRepo.configuration.sftp.Download;

import luiz.imageRepo.configuration.sftp.Common.SFTPProperties;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.gateway.SftpOutboundGateway;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

@Configuration
public class DownloadConfiguration {

    @Autowired
    public SFTPProperties properties;

    @Bean
    @ServiceActivator(inputChannel = "downloadFile")
    SftpOutboundGateway downloadHandler(SessionFactory<ChannelSftp.LsEntry> sessionFactory) {

        return new SftpOutboundGateway(sessionFactory, (session, requestMessage) -> {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            String basePath = properties.getRemoteDirectory();

            String path = basePath + "/" + requestMessage.getPayload().toString();

            session.read(path, outputStream);

            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
        });
    }
}

