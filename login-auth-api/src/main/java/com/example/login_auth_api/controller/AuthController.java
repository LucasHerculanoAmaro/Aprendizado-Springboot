package com.example.login_auth_api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dto.LoginRequestDTO;
import com.example.login_auth_api.dto.RegisterRequestDTO;
import com.example.login_auth_api.dto.ResponseDTO;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginRequestDTO body) {
		System.out.println("Tentativa de login com email: " + body.email());
		User user = repository.findByEmail(body.email())
				.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
		if(passwordEncoder.matches(body.password(), user.getPassword())) {
			String token = tokenService.generateToken(user);
			System.out.println("meu token: " + token);
			return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/register")
	public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
		Optional<User> user = repository.findByEmail(body.email());
		
		if(user.isEmpty()) {
			User newUser = new User();
			newUser.setPassword(passwordEncoder.encode(body.password()));
			newUser.setEmail(body.email());
			newUser.setName(body.name());
			repository.save(newUser);
			
			String token = tokenService.generateToken(newUser);
			return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
			
		}
		return ResponseEntity.badRequest().build();
	}
	
}
