package org.gangonem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RecruitingApp {

	public static void main(String[] args) {
		SpringApplication.run(RecruitingApp.class, args);
	}
}
