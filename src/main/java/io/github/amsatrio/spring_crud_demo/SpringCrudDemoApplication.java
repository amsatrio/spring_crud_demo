package io.github.amsatrio.spring_crud_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCrudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCrudDemoApplication.class, args);
	}

}
