package com.healthfirst.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.healthfirst.exception.ResourceAlreadyExitisException;
import com.healthfirst.exception.ResourceNotFoundException;
import com.healthfirst.repository.UserRepository;
import com.healthfirst.service.JwtService;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/signup")
	public ApiResponse<SignupResponse> signup(@RequestBody SignupRequestDto request) {
	Optional<UserEntity> optionalUser =	userRepository.findByEmail(request.getEmail());
	if (optionalUser.isPresent()) {
		throw new ResourceAlreadyExitisException("This email is already register with us.");
	}
	UserEntity entity = new UserEntity();
	entity.setEmail(request.getEmail());
	entity.setPassword(passwordEncoder.encode(request.getPassword()));
	entity.setMobileNumber(request.getMobileNumber());
	entity.setName(request.getName());
	entity.setRole("DOCTOR");
	UserEntity saveUser = userRepository.save(entity);
	SignupResponse response = new SignupResponse();
	response.setEmail(saveUser.getEmail());
	response.setId(saveUser.getId());
	response.setMobileNumber(saveUser.getMobileNumber());
	response.setName(saveUser.getName());
	return new ApiResponse<SignupResponse>(true, "Account is created successfully", response);
	}
	
	@PostMapping("/login")
	public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
	Authentication authentication =	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
	UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ResourceNotFoundException("This email is register with us"));
	LoginResponseDto responseDto = new LoginResponseDto();
	responseDto.setEmail(user.getEmail());
	responseDto.setId(user.getId());
	responseDto.setMobileNumber(user.getMobileNumber());
	responseDto.setToken(jwtService.genrateJwtToken(user.getEmail()));
	responseDto.setName(user.getName());
		return  new ApiResponse<LoginResponseDto>(true, "User logged in successfully", responseDto);
	}
}
