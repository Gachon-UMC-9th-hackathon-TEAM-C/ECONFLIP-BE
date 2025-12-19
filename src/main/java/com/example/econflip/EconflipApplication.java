package com.example.econflip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EconflipApplication {

	public static void main(String[] args) {
		SpringApplication.run(EconflipApplication.class, args);
	}

}
