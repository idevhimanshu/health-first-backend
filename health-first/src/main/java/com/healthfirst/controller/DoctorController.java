package com.healthfirst.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthfirst.dto.ApiResponse;
import com.healthfirst.dto.DoctorProfileRequest;
import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.repository.DoctorRepository;
import com.healthfirst.repository.UserRepository;
import com.healthfirst.service.DoctorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("api/doctor")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private DoctorService doctorService;

	@PostMapping("/doctor-profile")
	public DoctorEntity createDoctorProfile(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody DoctorProfileRequest request) {

		UserEntity user = userRepository.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!"DOCTOR".equals(user.getRole())) {
			throw new RuntimeException("Only doctor can create doctor profile");
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

		return doctorRepository.save(doctor);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/doctors")
	public ApiResponse<List<DoctorEntity>> getDoctorList() {
		return doctorService.getAllDoctorList();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/search")
	public ApiResponse<List<DoctorEntity>> search(@RequestParam String name){
		return doctorService.search(name);
	}

}
