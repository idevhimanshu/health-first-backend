package com.healthfirst.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DoctorProfileResponseDto {
	
	private String name;
	private String image;
	private String gender; 
	private String specialization;
	private String qualification;
	private String licenseNumber;
	private int experienceyear;
	private int consultantFee;
	private String hospitalName;
	private String address;
	private String city;
	private String bio;
	
	
}
