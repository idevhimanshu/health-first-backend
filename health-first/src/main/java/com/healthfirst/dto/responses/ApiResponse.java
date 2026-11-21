package com.healthfirst.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {
	
	private boolean status;
	private String message;
	private T data;
	
	
	public static <T> ApiResponse<T> success(String mesaage, T data) {
		return new ApiResponse<T>(true, mesaage, data);
		
	}
	
	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<>(false, message, null);
	}
}
