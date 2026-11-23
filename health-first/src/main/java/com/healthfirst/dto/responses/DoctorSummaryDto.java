package com.healthfirst.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorSummaryDto {
	
	private long id;
	private String name;
	private String gender;
	private String image;
	private String specializaiton;
	private int commentCount;
	private double rating;
	private boolean isFavorite;

}
