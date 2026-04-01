package com.kodebytes;

import com.kodebytes.model.Person;
import com.kodebytes.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods // New activator for SB4 resilience features
public class Application {

	static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PersonRepository repository) {
		return args -> {
			repository.save(new Person(null,"Antonio", "Casado", "a.casado@gmail.com", "123456"));
			repository.save(new Person(null,"John", "Smith", "j.smith@gmail.com", "234567"));
			repository.save(new Person(null,"Barbara", "Ray", "b.ray@gmail.com", "345678"));
			repository.save(new Person(null,"Richard", "Standridge", "r.standridge@gmail.com", "456789"));
			repository.save(new Person(null,"Andrew", "Orleans", "a.orleans@gmail.com", "567890"));
		};
	}
}
