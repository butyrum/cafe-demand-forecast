package com.cafe.forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // ✅ Habilita @Scheduled
public class ForecastApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForecastApplication.class, args);
	}
}
