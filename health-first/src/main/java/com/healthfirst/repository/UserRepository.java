package com.healthfirst.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	Optional<UserEntity> findByEmail(String email);
}
