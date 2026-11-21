package com.healthfirst.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.healthfirst.dto.requests.LoginRequestDto;
import com.healthfirst.dto.responses.ApiResponse;
import com.healthfirst.dto.responses.LoginResponseDto;
import com.healthfirst.dto.responses.RefreshTokenResponseDto;
import com.healthfirst.entity.RefreshTokenEntity;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.entity.UsersPrincipal;
import com.healthfirst.repository.RefreshTokenRepository;
import com.healthfirst.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RefreshTokenRepository refreshTokenRepository;
	

	public ApiResponse<LoginResponseDto> login(LoginRequestDto request, String role) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getEmail().toLowerCase().trim(), request.getPassword()));
		UsersPrincipal userPrincipal = (UsersPrincipal) authentication.getPrincipal();
		UserEntity userEntity = userPrincipal.getUser();
		if (!role.equalsIgnoreCase(userEntity.getRole())) {
			throw new AccessDeniedException("Unauthorized access. Please use the appropriate app.");
		}
		return buildResponse(userEntity);
	}
	
	@Transactional
	public ApiResponse<Void> logout(String refreshToken) {
	RefreshTokenEntity entity =	refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("Given token wrong"));
	entity.setRevoked(true);
	refreshTokenRepository.save(entity);
	return ApiResponse.success("Logout successfully", null);
	}
	
	public ApiResponse<RefreshTokenResponseDto> refresh(String refreshToken) {
	 RefreshTokenEntity tokenEntity =	refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new RuntimeException("refresh token not found"));
	 if (tokenEntity.getExpDate().isBefore(LocalDateTime.now()) || tokenEntity.isRevoked()) {
		 refreshTokenRepository.delete(tokenEntity);
		 throw new RuntimeException("refresh token expired please login again");
	 }
	 UserEntity user = tokenEntity.getUser();
	 String newAccessToken = jwtService.genrateJwtToken(user);
	 RefreshTokenResponseDto response = RefreshTokenResponseDto.builder().accessToken(newAccessToken).refreshToken(refreshToken).build();
	 return ApiResponse.success("New Access token generated",response );
	}

	public ApiResponse<LoginResponseDto> buildResponse(UserEntity user) {
		String accessToken = jwtService.genrateJwtToken(user);
		String refreshToken = jwtService.genrateRefreshToken(user);
		RefreshTokenEntity token = new RefreshTokenEntity();
		token.setRevoked(false);
		token.setToken(refreshToken);
		token.setUser(user);
		token.setExpDate(LocalDateTime.now().plusDays(7));
		refreshTokenRepository.save(token);

		LoginResponseDto response = new LoginResponseDto();
		response.setId(user.getId());
		response.setEmail(user.getEmail());
		response.setMobileNumber(user.getMobileNumber());
		response.setName(user.getName());
		response.setRole(user.getRole());
		response.setTokenType("Bearer");
		response.setAccessToken(accessToken);
		response.setRefreshToken(refreshToken);
		return ApiResponse.success("Login Successfull.", response);

	}

}
