package com.idiotBox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
@PropertySource("classpath:application.properties")
})
@SpringBootApplication
public class IdiotBoxApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdiotBoxApplication.class, args);
	}
}