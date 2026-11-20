package com.healthfirst.controller;


import org.springframework.security.access.AccessDeniedException;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthfirst.dto.ApiResponse;
import com.healthfirst.dto.LoginRequestDto;
import com.healthfirst.dto.LoginResponseDto;
import com.healthfirst.dto.SignupRequestDto;
import com.healthfirst.dto.SignupResponse;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.entity.UsersPrincipal;
import com.healthfirst.exception.ResourceAlreadyExitisException;
import com.healthfirst.exception.ResourceNotFoundException;
import com.healthfirst.repository.UserRepository;
import com.healthfirst.service.AuthService;
import com.healthfirst.service.CustomUserDetailService;
import com.healthfirst.service.JwtService;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	
	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthService authService;
	
	@PostMapping("patient/signup")
	public ApiResponse<SignupResponse> patientSignup(@RequestBody SignupRequestDto request) {
	Optional<UserEntity> optionalUser =	userRepository.findByEmail(request.getEmail());
	if (optionalUser.isPresent()) {
		throw new ResourceAlreadyExitisException("This email is already register with us.");
	}
	UserEntity entity = new UserEntity();
	entity.setEmail(request.getEmail());
	entity.setPassword(passwordEncoder.encode(request.getPassword()));
	entity.setMobileNumber(request.getMobileNumber());
	entity.setName(request.getName());
	entity.setRole("ROLE_USER");
	UserEntity saveUser = userRepository.save(entity);
	SignupResponse response = new SignupResponse();
	response.setEmail(saveUser.getEmail());
	response.setId(saveUser.getId());
	response.setMobileNumber(saveUser.getMobileNumber());
	response.setName(saveUser.getName());
	return new ApiResponse<SignupResponse>(true, "Account is created successfully", response);
	}
	
	@PostMapping("doctor/signup")
	public ApiResponse<SignupResponse> doctorSignup(@RequestBody SignupRequestDto request) {
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
	SignupResponse response = new SignupResponse();
	response.setEmail(saveUser.getEmail());
	response.setId(saveUser.getId());
	response.setMobileNumber(saveUser.getMobileNumber());
	response.setName(saveUser.getName());
	return new ApiResponse<SignupResponse>(true, "Account is created successfully", response);
	}
	
	@PostMapping("patient/login")
	public ApiResponse<LoginResponseDto> patientLogin(@RequestBody LoginRequestDto request) {
		return authService.login(request, "ROLE_USER");
	}
	
	@PostMapping("doctor/login")
	public ApiResponse<LoginResponseDto> doctorLogin(@RequestBody LoginRequestDto request) {
		return authService.login(request, "ROLE_DOCTOR");
	}
	
	@PostMapping("doctor/logout")
	public void doctorLogout(@RequestBody String refreshToken) {
		authService.logout(refreshToken);
	}
	
}
