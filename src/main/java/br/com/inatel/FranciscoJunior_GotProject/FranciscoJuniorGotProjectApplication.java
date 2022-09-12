package br.com.inatel.FranciscoJunior_GotProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * SpringBoot application about Game of Thrones
 * @author francisco.carvalho
 * @since 1.0
 */
@SpringBootApplication
@EnableCaching
public class FranciscoJuniorGotProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FranciscoJuniorGotProjectApplication.class, args);
	}

}
