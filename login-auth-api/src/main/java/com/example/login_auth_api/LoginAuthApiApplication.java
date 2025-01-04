package com.example.login_auth_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import jakarta.annotation.PreDestroy;

@SpringBootApplication
public class LoginAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginAuthApiApplication.class, args);
	}
	
	@PreDestroy
    public void onDestroy() {
        System.out.println("Application shutting down...");
    }

}
