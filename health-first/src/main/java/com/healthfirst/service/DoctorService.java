package com.healthfirst.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.repository.DoctorRepository;

@Service
public class DoctorService {

	
 private DoctorRepository doctorRepository;
 
 public DoctorService(DoctorRepository doctorRepository) {
	 this.doctorRepository = doctorRepository;
 }
 
 public List<DoctorEntity> getAllDoctorList(){
	 return doctorRepository.findAll();
 }
	
}
