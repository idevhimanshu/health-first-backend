package com.healthfirst.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoctorProfileRequestDto {
	
	@Schema(example = "Cardiology")
	private String specialization;
	
	@Schema(example = "MBBS, MD (MEDICINE), DM (CARDIOLOGY)")
    private String qualification;
	
	@Schema(example = "DMC/R/12084")
    private String licenseNumber;
	
	@Schema(example = "12 years")
    private int experienceyear;
	
	@Schema(example = "1500")
    private int consultantFee;
	
	@Schema(example = "Aiim, New Delhi")
    private String hospitalName;
	
	@Schema(example = "Ansari Nager, New Delhi")
    private String address;
	
	@Schema(example = "New Delhi")
    private String city;
	
	@Schema(example = "Consultant cardiologist specializing in cardiac electrophysiology and pacing, with advanced training in cardiology and over 7 years of experience.")
    private String bio;

}
