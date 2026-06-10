package com.example.pipeup;

import com.example.pipeup.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; // Importar
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration; // Importar
import org.springframework.scheduling.annotation.EnableScheduling; // Habilitar tarefas agendadas

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class}) // Adicionar esta parte
@Import(SecurityConfig.class)
@EnableScheduling // RF-08: permite a verificação automática de atraso
public class PipeUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PipeUpApplication.class, args);
	}

}