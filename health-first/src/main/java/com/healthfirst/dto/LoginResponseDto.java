package com.healthfirst.dto;

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
	private String password;
	private String mobileNumber;
	private String token;
}
