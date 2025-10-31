package com.bufete.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
public class BackendBufeteApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendBufeteApplication.class, args);

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		System.out.println();
		System.out.println("...........");
		System.out.println("Server Running");
		System.out.println("...........");
		System.out.println();
	}

}
