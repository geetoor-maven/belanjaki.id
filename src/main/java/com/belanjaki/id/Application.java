package com.belanjaki.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.belanjaki.id.*")
@EntityScan(basePackages = "com.belanjaki.id.*")
@SpringBootApplication(scanBasePackages = "com.belanjaki.id")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
