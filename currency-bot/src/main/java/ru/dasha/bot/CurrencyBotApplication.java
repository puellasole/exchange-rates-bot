package ru.dasha.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class CurrencyBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurrencyBotApplication.class, args);
	}
}
