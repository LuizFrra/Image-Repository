package luiz.imageRepo.configuration.security.main;

import luiz.imageRepo.configuration.security.filters.JWTAuthenticationFilter;
import luiz.imageRepo.configuration.security.filters.JWTValidationFilter;
import luiz.imageRepo.configuration.security.handlers.JWTAccessDeniedHandler;
import luiz.imageRepo.configuration.security.handlers.JWTAuthenticationFailureHandler;
import luiz.imageRepo.configuration.security.jwt.JWTKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public JWTKeyGenerator jwtKeyGenerator;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers("/v1/user**").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(authenticationFilter())
                .addFilter(validationFilter())
                .exceptionHandling().accessDeniedHandler(new JWTAccessDeniedHandler()).and()
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTAuthenticationFilter authenticationFilter() throws Exception {
        JWTAuthenticationFilter JWTAuthenticationFilter = new JWTAuthenticationFilter();
        JWTAuthenticationFilter.setAuthenticationManager(authenticationManager());
        JWTAuthenticationFilter.setFilterProcessesUrl("/api/auth");
        JWTAuthenticationFilter.setPostOnly(true);
        JWTAuthenticationFilter.setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
        return JWTAuthenticationFilter;
    }

    JWTValidationFilter validationFilter() throws Exception {
        JWTValidationFilter JWTValidationFilter = new JWTValidationFilter(authenticationManager());
        JWTValidationFilter.setJwtVerifier(jwtKeyGenerator.getAlgorithm());
        return JWTValidationFilter;
    }
}
