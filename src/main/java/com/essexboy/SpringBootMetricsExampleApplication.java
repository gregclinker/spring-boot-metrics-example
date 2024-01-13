package com.essexboy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootMetricsExampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMetricsExampleApplication.class, args);
	}
}
