package com.healthfirst.controller;

import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthfirst.dto.ApiResponse;
import com.healthfirst.dto.LoginRequestDto;
import com.healthfirst.dto.LoginResponseDto;
import com.healthfirst.dto.RefreshTokenRequestDto;
import com.healthfirst.dto.RefreshTokenResponseDto;
import com.healthfirst.dto.SignupRequestDto;
import com.healthfirst.dto.SignupResponse;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.exception.ResourceAlreadyExitisException;
import com.healthfirst.repository.UserRepository;
import com.healthfirst.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
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
	
	
	
	@PostMapping("patient/login")
	public ApiResponse<LoginResponseDto> patientLogin(@RequestBody LoginRequestDto request) {
		return authService.login(request, "ROLE_USER");
	}
	
	@PostMapping("patient/logout")
	public ApiResponse<Void> doctorLogout(@RequestBody RefreshTokenRequestDto refreshToken) {
		return authService.logout(refreshToken.getToken());
	}
	
	
	@PostMapping("/refresh")
	public ApiResponse<RefreshTokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshToken){
		return authService.refresh(refreshToken.getToken());
	}
	

}
