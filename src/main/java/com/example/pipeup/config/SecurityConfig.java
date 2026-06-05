package com.example.pipeup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configurando SecurityFilterChain...");
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Recursos estáticos — sem autenticação
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/css/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/js/**")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/img/**")).permitAll()
                        // Login — sem autenticação
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).permitAll()
                        // Tudo o mais exige login
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                // CSRF: desabilitado apenas para os POST internos de form
                // que o Thymeleaf não inclui token automaticamente
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/empresas"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/empresas/editar"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/empresas/excluir"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/espacos"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/espacos/editar"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/espacos/excluir"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/tarefas/nova"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/tarefas/*"),
                                AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/tarefas/*/atividades")
                        )
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        logger.info("SecurityFilterChain configurado.");
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Configurando UserDetailsService com usuário 'user'.");
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}