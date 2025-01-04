package com.example.login_auth_api.infra.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.hibernate.annotations.AnyDiscriminator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		var token = this.recoverToken(request);
		var login = tokenService.validateToken(token);
		
		if (login != null) {
			User user = userRepository.findByEmail(login)
					.orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
			var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
			var authentication = new UsernamePasswordAuthenticationToken(
					user, null, authorities );
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	public String recoverToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		if(authHeader == null) return null;
		return authHeader.replace("Bearer ", "");
				
	}

}
