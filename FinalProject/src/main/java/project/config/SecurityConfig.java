package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import project.services.AppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final AppUserService appUserService;

    @Autowired
    public SecurityConfig(AppUserService appUserService) {
        this.appUserService = appUserService;
    }    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/contact").permitAll()
                    .requestMatchers("/store/**").permitAll()
                    .requestMatchers("/register").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/logout").permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .defaultSuccessUrl("/", true)
            )
            .logout(config -> config.logoutSuccessUrl("/")
            )
            .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
}
