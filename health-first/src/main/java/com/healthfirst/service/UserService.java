package com.healthfirst.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.healthfirst.dto.requests.ChangePasswordRequestDto;
import com.healthfirst.dto.requests.UpdateProfileRequestDto;
import com.healthfirst.dto.responses.ApiResponse;
import com.healthfirst.dto.responses.UserDetailsDto;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.repository.UserRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private S3Presigner s3Presigner;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private S3Client s3Client;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	private final Path uploadDir = Paths.get("uploads");
	
	public ApiResponse<UserDetailsDto> getUserDetails(Long id){
		UserEntity userEntity = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User name not found" + id));
		UserDetailsDto dtoResponse = new	UserDetailsDto(userEntity.getId(), userEntity.getName(), userEntity.getGender());
		return ApiResponse.success("Success", dtoResponse);
	}
	
	

	public ApiResponse<UserEntity> updateProfile(String email, UpdateProfileRequestDto request) {
		UserEntity userEntity = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User name not found" + email));
		if (request.getName() != null)
			userEntity.setName(request.getName());
		if (request.getMobileNumber() != null)
			userEntity.setMobileNumber(request.getMobileNumber());

		return new ApiResponse<UserEntity>(true, "Profile updated successfully", userRepository.save(userEntity));
	}

	public ApiResponse<UserEntity> updatePassword(String username, ChangePasswordRequestDto request) {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User name not found" + username));
		if (passwordEncoder.matches(request.getCurrentPassword(), userEntity.getPassword())) {
			userEntity.setPassword(passwordEncoder.encode(request.getNewPassword()));
			return new ApiResponse<UserEntity>(true, "Password has been changed", userRepository.save(userEntity));
		} else {
			throw new RuntimeException("Password is not correct.");
		}
	}

	public String uploadImage(String path, MultipartFile file) throws IOException {

		String originalFilename = file.getOriginalFilename();

		if (file.isEmpty() || originalFilename == null || originalFilename.isBlank()) {
			throw new IllegalArgumentException("Please upload a valid image");
		}

		String extension = "";
		int dotIndex = originalFilename.lastIndexOf(".");

		if (dotIndex >= 0) {
			extension = originalFilename.substring(dotIndex);
		}

		String newFileName = UUID.randomUUID() + extension;

		String filePath = path + "/" + newFileName;

		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(filePath)
				.contentType(file.getContentType()).build();

		s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

		return newFileName;
	}

	public String getPresigner(String objectKey) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();

		GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
				.signatureDuration(Duration.ofHours(1)).getObjectRequest(getObjectRequest).build();

		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

		return presignedGetObjectRequest.url().toString();

	}

}
