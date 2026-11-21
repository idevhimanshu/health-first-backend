package com.healthfirst.controller;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthfirst.dto.requests.LoginRequestDto;
import com.healthfirst.dto.requests.RefreshTokenRequestDto;
import com.healthfirst.dto.requests.SignupRequestDto;
import com.healthfirst.dto.responses.ApiResponse;
import com.healthfirst.dto.responses.LoginResponseDto;
import com.healthfirst.dto.responses.SignupResponseDto;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.exception.ResourceAlreadyExitisException;
import com.healthfirst.repository.UserRepository;
import com.healthfirst.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class DoctorAuthController {
	
	private final AuthService authService;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("doctor/signup")
	public ApiResponse<SignupResponseDto> doctorSignup(@RequestBody SignupRequestDto request) {
	Optional<UserEntity> optionalUser =	userRepository.findByEmail(request.getEmail());
	if (optionalUser.isPresent()) {
		throw new ResourceAlreadyExitisException("This email is already register with us.");
	}
	UserEntity entity = new UserEntity();
	entity.setEmail(request.getEmail());
	entity.setPassword(passwordEncoder.encode(request.getPassword()));
	entity.setMobileNumber(request.getMobileNumber());
	entity.setName(request.getName());
	entity.setRole("ROLE_DOCTOR");
	UserEntity saveUser = userRepository.save(entity);
	SignupResponseDto response = new SignupResponseDto();
	response.setEmail(saveUser.getEmail());
	response.setId(saveUser.getId());
	response.setMobileNumber(saveUser.getMobileNumber());
	response.setName(saveUser.getName());
	return new ApiResponse<SignupResponseDto>(true, "Account is created successfully", response);
	}
	
	
	
	@PostMapping("doctor/login")
	public ApiResponse<LoginResponseDto> doctorLogin(@RequestBody LoginRequestDto request) {
		return authService.login(request, "ROLE_DOCTOR");
	}
	
	@PostMapping("doctor/logout")
	public void doctorLogout(@RequestBody RefreshTokenRequestDto refreshToken) {
		authService.logout(refreshToken.getToken());
	}
	

}
