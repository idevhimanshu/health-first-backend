package com.healthfirst.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.healthfirst.dto.ApiResponse;
import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.repository.DoctorRepository;

@Service
public class DoctorService {

	
 private DoctorRepository doctorRepository;
 
 public DoctorService(DoctorRepository doctorRepository) {
	 this.doctorRepository = doctorRepository;
 }
 
 public ApiResponse<List<DoctorEntity>> getAllDoctorList(){
	 List<DoctorEntity> doctorList = doctorRepository.findAll();
	return ApiResponse.success("Success", doctorList);
 }
	
}
