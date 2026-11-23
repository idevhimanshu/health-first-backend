package com.healthfirst.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthfirst.dto.requests.DoctorProfileRequestDto;
import com.healthfirst.dto.responses.ApiResponse;
import com.healthfirst.dto.responses.DoctorProfileResponseDto;
import com.healthfirst.dto.responses.DoctorSummaryDto;
import com.healthfirst.entity.DoctorEntity;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.exception.ResourceAlreadyExitisException;
import com.healthfirst.repository.DoctorRepository;
import com.healthfirst.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

	private final DoctorRepository doctorRepository;
	private final UserRepository userRepository;

	public ApiResponse<List<DoctorSummaryDto>> getAllDoctorList() {
		List<DoctorEntity> doctorList = doctorRepository.findAll();
		List<DoctorSummaryDto> dtoList = doctorList.stream()
				.map(doctor ->  new DoctorSummaryDto(doctor.getUser().getName(), doctor.getUser().getGender(), doctor.getUser().getImage(), doctor.getSpecialization(), 0, 0, false))
				.toList();
		return ApiResponse.success("Success", dtoList);
	}

	public ApiResponse<List<DoctorSummaryDto>> search(String name) {
		List<DoctorEntity> doctorList = doctorRepository
				.findBySpecializationContainingIgnoreCaseOrHospitalNameContainingIgnoreCase(name, name);
		List<DoctorSummaryDto> dtoList = doctorList.stream()
				.map(doctor ->  new DoctorSummaryDto(doctor.getUser().getName(), doctor.getUser().getGender(), doctor.getUser().getImage(), doctor.getSpecialization(), 0, 0, false))
				.toList();
		return ApiResponse.success("Success", dtoList);
	}

	public ApiResponse<DoctorProfileResponseDto> createDoctorProfile(UserDetails user,
			DoctorProfileRequestDto request) {
		UserEntity userEntity = userRepository.findByEmail(user.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User is not register with us"));

		if (userEntity.getRole() != "ROLE_DOCTOR") {
			throw new AccessDeniedException("Only doctor can access this resource");
		}

		if (doctorRepository.existsByUser_Id(userEntity.getId())) {
			throw new ResourceAlreadyExitisException("This account profile is already created");
		}

		DoctorEntity doctorEntity = DoctorEntity.builder().specialization(request.getSpecialization())
				.qualification(request.getSpecialization()).licenseNumber(request.getLicenseNumber())
				.experienceyear(request.getExperienceyear()).hospitalName(request.getHospitalName())
				.consultantFee(request.getConsultantFee()).address(request.getAddress()).bio(request.getBio())
				.city(request.getCity()).user(userEntity).build();

		DoctorEntity responseEntity = doctorRepository.save(doctorEntity);

		DoctorProfileResponseDto responseDto = DoctorProfileResponseDto.builder()
				.name(responseEntity.getUser().getName()).gender(responseEntity.getUser().getGender())
				.specialization(responseEntity.getSpecialization()).qualification(responseEntity.getQualification())
				.licenseNumber(responseEntity.getLicenseNumber()).experienceyear(responseEntity.getExperienceyear())
				.consultantFee(responseEntity.getConsultantFee()).hospitalName(responseEntity.getHospitalName())
				.address(responseEntity.getAddress()).city(responseEntity.getCity()).bio(responseEntity.getBio())
				.build();

		return ApiResponse.success("Profile Created Successfully", responseDto);

	}

}
