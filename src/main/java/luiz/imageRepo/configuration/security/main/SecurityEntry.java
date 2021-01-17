package luiz.imageRepo.configuration.security.main;

import luiz.imageRepo.configuration.security.jwt.JWTKeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityEntry {

    @Value("${security.symetricKey}")
    public String symetricKey;

    @Bean
    public JWTKeyGenerator generateRSA() throws Exception {
        JWTKeyGenerator jwtKeyGenerator = new JWTKeyGenerator();
        jwtKeyGenerator.setKeySize(512);
        jwtKeyGenerator.useRSA = false;
        jwtKeyGenerator.setSymmetricKey(symetricKey);
        jwtKeyGenerator.generateKey();
        return jwtKeyGenerator;
    }

}
