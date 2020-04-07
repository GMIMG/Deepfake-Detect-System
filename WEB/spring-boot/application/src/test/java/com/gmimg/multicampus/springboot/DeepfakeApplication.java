package com.gmimg.multicampus.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.gmimg.multicampus.springboot.storage.StorageProperties;
import com.gmimg.multicampus.springboot.storage.StorageService;



@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DeepfakeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeepfakeApplication.class, args);
	}


	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}

}
