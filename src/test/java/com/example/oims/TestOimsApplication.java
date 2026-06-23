package com.example.oims;

import org.springframework.boot.SpringApplication;

public class TestOimsApplication {

	public static void main(String[] args) {
		SpringApplication.from(OimsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
