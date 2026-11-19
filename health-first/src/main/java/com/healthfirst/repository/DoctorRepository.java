package com.healthfirst.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.healthfirst.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {
	
	boolean existsByUser_Id(Long id);
	List<DoctorEntity> findAll();
	List<DoctorEntity> findBySpecializationContainingIgnoreCaseOrHospitalNameContainingIgnoreCase(String specialization, String hospitalName);
}