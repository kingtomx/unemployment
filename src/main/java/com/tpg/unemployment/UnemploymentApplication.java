package com.tpg.unemployment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
@EnableAsync
public class UnemploymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnemploymentApplication.class, args);
	}

}
