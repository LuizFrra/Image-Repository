package luiz.imageRepo.configuration.sftp.Common;

import com.jcraft.jsch.ChannelSftp;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpRemoteFileTemplate;

@Configuration
@AllArgsConstructor
public class CommonConfiguration {

    public final SFTPProperties properties;

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sessionFactory() {
        DefaultSftpSessionFactory sessionFactory = new DefaultSftpSessionFactory(true);
        sessionFactory.setUser(properties.getUsername());
        sessionFactory.setPassword(properties.getPassword());
        sessionFactory.setHost(properties.getHost());
        sessionFactory.setPort(properties.getPort());
        sessionFactory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(sessionFactory);
    }

    @Bean
    public SftpRemoteFileTemplate remoteFileTemplate(SessionFactory<ChannelSftp.LsEntry> sessionFactory) {
        return new SftpRemoteFileTemplate(sessionFactory);
    }


}
