package io.github.amsatrio.spring_crud_demo;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MBiodata;
import io.github.amsatrio.spring_crud_demo.modules.hospital.entity.MRole;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MBiodataService;
import io.github.amsatrio.spring_crud_demo.modules.hospital.service.MRoleService;
import io.github.amsatrio.spring_crud_demo.util.Generator;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableCaching
public class SpringCrudDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCrudDemoApplication.class, args);
	}

}
