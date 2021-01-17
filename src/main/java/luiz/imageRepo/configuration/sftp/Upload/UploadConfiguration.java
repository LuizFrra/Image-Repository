package luiz.imageRepo.configuration.sftp.Upload;

import lombok.AllArgsConstructor;
import luiz.imageRepo.configuration.sftp.Common.SFTPProperties;

import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;

import java.time.LocalDate;

@Configuration
@AllArgsConstructor
public class UploadConfiguration {

    public final SFTPProperties properties;

    @Bean
    @ServiceActivator(inputChannel = "uploadFile")
    SftpMessageHandler uploadHandler(SessionFactory<ChannelSftp.LsEntry> sessionFactory) {
        SftpMessageHandler messageHandler = new SftpMessageHandler(sessionFactory);
        messageHandler.setRemoteDirectoryExpression(new LiteralExpression(properties.getRemoteDirectory()));

        messageHandler.setFileNameGenerator(message -> {
            String fileName = FilenameUtils.getName(message.getPayload().toString());
            return String.format(fileName, LocalDate.now());
        });

        return messageHandler;
    }

}

