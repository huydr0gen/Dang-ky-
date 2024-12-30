package com.tlu.dangkyhoc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tlu.dangkyhoc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	Optional<User> findByResetToken(String token);
}
