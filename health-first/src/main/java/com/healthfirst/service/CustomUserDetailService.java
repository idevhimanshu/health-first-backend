package com.healthfirst.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.healthfirst.entity.UsersPrincipal;
import com.healthfirst.exception.ResourceNotFoundException;
import com.healthfirst.entity.UserEntity;
import com.healthfirst.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
		// TODO Auto-generated method stub
		UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("This email is not register with us."));
		return new UsersPrincipal(userEntity);
	}
	
	
}
