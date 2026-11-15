package com.healthfirst.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdateProfileRequestDto {
	
	private String name;
	private String mobileNumber;

}
