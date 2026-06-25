package dev.keeneye;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KeeneyeApplication {
	private static Initializer initiator;
	@Autowired
	public void setInitialLoader(Initializer initiator) {
		KeeneyeApplication.initiator = initiator;
	}
	public static void main(String[] args) {
		SpringApplication.run(KeeneyeApplication.class, args);
		initiator.initial();
	}

}
