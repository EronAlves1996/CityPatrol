package io.eronalves1996.citypatrolback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "City Patrol API", description = "Documentation for City Patrol API endpoints"))
public class CitypatrolbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitypatrolbackApplication.class, args);
	}

}
