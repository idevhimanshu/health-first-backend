package com.healthfirst.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponseDto {

	private long id;
	private String name;
	private String email;
	private String mobileNumber;
	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private String role;
	
}
