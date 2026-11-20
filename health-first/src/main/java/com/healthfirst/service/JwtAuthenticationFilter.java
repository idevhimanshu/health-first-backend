package com.healthfirst.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	
	private final JwtService jwtService;
	private final CustomUserDetailService customUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		String header = request.getHeader("Authorization");
		String token = null;
		String username = null;
		String role = null;
		Long userId = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			username = jwtService.extractUsername(token);
			role = jwtService.extractRole(token);
			userId = jwtService.extractUserId(token);
			
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			System.out.println("Role before formatted:" + role);
			String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
			System.out.println("Role after formatted " + formattedRole);
			List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(formattedRole));
				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			
		}

		filterChain.doFilter(request, response);

	}

}
