package com.healthfirst.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.healthfirst.dto.ApiResponse;
import com.healthfirst.dto.ChangePasswordRequestDto;
import com.healthfirst.dto.UpdateProfileRequestDto;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.service.UserService;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Value("${project.image}")
	private String path;
	
	
	@PutMapping("/profile")
	public ApiResponse<UserEntity> updateProfile(@RequestBody UpdateProfileRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
		return userService.updateProfile(userDetails.getUsername(), request);
	}
	
	@PutMapping("/change-password")
	public ApiResponse<UserEntity> updatePassword(@RequestBody ChangePasswordRequestDto request, @AuthenticationPrincipal UserDetails userDetails){
		return userService.updatePassword(userDetails.getUsername(), request);
	}
	
	@PostMapping(
		    value = "/profile-image",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
		)
		public ApiResponse<String> updateImage(
		        @RequestParam("file") MultipartFile file) {

		    try {
		        String fileName = userService.uploadImage(path, file);
		        String objectKey = path + "/" + fileName;
		        String url = userService.getPresigner(objectKey);

		        return ApiResponse.success(
		            "Image uploaded successfully",
		            url
		        );

		    } catch (IOException e) {
		        throw new RuntimeException("Failed to upload profile image", e);
		    }
		}
	
}
