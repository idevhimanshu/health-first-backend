package com.healthfirst.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
	
	@Schema(example = "Rahul Sharma")
	private String name;
	
	@Schema(example = "9264578124")
	private String mobileNumber;
	
	@Schema(example = "rahuldoctor@gmail.com")
	private String email;
	
	@Schema(example = "Rahul@123")
	private String password;
	
}
