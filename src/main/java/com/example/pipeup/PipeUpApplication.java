package com.example.pipeup;

import com.example.pipeup.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // Importar
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration; // Importar

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class}) // Adicionar esta parte
@Import(SecurityConfig.class)
public class PipeUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipeUpApplication.class, args);
	}

}