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
import com.healthfirst.dto.requests.DoctorProfileRequestDto;
import com.healthfirst.dto.responses.ApiResponse;
import com.healthfirst.dto.responses.DoctorProfileResponseDto;
import com.healthfirst.dto.responses.DoctorSummaryDto;
import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.service.DoctorService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/doctor")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {
	
	
	private final DoctorService doctorService;

	@PostMapping("/doctor-profile")
	@PreAuthorize("hasRole(ROLE_DOCTOR")
	public ApiResponse<DoctorProfileResponseDto> createDoctorProfile(@AuthenticationPrincipal UserDetails userDetails,
			@RequestBody DoctorProfileRequestDto request) {

		return doctorService.createDoctorProfile(userDetails, request);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/doctors")
	public ApiResponse<List<DoctorSummaryDto>> getDoctorList() {
		return doctorService.getAllDoctorList();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/search")
	public ApiResponse<List<DoctorEntity>> search(@RequestParam(value = "specializaiton", required = false) String name){
		return doctorService.search(name);
	}

}
