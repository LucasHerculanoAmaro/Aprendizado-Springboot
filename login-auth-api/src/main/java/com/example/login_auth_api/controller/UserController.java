package com.example.login_auth_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@GetMapping
	public ResponseEntity<String> getUser() {
		System.out.println("Sucesso!");
		return ResponseEntity.ok("sucesso!");
	}
}
