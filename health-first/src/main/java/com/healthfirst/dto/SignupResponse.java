package com.healthfirst.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
	
	private Long id;
	private String name;
	private String mobileNumber;
	private String email;

}
