package com.healthfirst.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "doctor_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DoctorEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String specialization;
	private String qualification;
	private String licenseNumber;
	private int experienceyear;
	private int consultantFee;
	private String hospitalName;
	private String address;
	private String city;
	private String bio;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true, nullable = false)
	private UserEntity user;
	
	
}
