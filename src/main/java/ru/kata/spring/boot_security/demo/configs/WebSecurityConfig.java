package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index", "/new").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("ADMIN", "USER")
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .usernameParameter("username").passwordParameter("password")
                        .loginPage("/login").loginProcessingUrl("/login").successHandler(successUserHandler)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/login") // Куда перенаправить после выхода
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}