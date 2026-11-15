package com.healthfirst.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import java.util.Base64.Decoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;

	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	public String genrateJwtToken(String email) {
		return Jwts.builder().subject(email).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)).signWith(getSigningKey())
				.compact();
	}

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getExpiration().before(new Date());

	}

}
