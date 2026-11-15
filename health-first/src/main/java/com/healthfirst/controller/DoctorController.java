package com.healthfirst.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthfirst.dto.DoctorProfileRequest;
import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.repository.DoctorRepository;
import com.healthfirst.repository.UserRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("api/doctor")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DoctorRepository doctorRepository;

	@PostMapping("/doctor-profile")
	public DoctorEntity createDoctorProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody DoctorProfileRequest request) {
		System.out.println(request);
	 UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
	 if (user.getRole() != "DOCTOR") {
		throw new RuntimeException("only doctor can create doctor profile");
	}
	 if (doctorRepository.existsByUser_Id(user.getId())) {
		throw new RuntimeException("Doctor profile already created");
		
	}
	 
	 DoctorEntity doctor = new DoctorEntity();
	 doctor.setUser(user);
	 doctor.setSpecialization(request.getSpecialization());
	    doctor.setQualification(request.getQualification());
	    doctor.setLicenseNumber(request.getLicenseNumber());
	    doctor.setExperienceyear(request.getExperienceyear());
	    doctor.setConsultantFee(request.getConsultantFee());
	    doctor.setHospitalName(request.getHospitalName());
	    doctor.setAddress(request.getAddress());
	    doctor.setCity(request.getCity());
	    doctor.setBio(request.getBio());
	    System.out.println(request);
	    System.out.println(doctor);
	 return doctorRepository.save(doctor);
	}
}
