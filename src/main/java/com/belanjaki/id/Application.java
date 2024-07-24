package com.belanjaki.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.belanjaki.id.*")
@EntityScan(basePackages = "com.belanjaki.id.*")
@SpringBootApplication(scanBasePackages = "com.belanjaki.id")
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
