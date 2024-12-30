package com.tlu.dangkyhoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                		.requestMatchers("/login","/login/forgot-password").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/teacher/**").hasAnyRole("ADMIN", "TEACHER")
//                        .requestMatchers("/student/**").hasAnyRole("ADMIN", "TEACHER","STUDENT")
//                        .anyRequest().authenticated())
//                .httpBasic(httpBasic -> {});
        
        		.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		return http.build();
	}
}
