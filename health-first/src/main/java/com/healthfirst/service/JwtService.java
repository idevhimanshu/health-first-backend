package com.healthfirst.service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.healthfirst.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secretKey;
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	@Value("${jwt.refresh-expiration}")
	private long refreshExpiration;

	
	private Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
	}

	

	public String extractUsername(String token) {
		// TODO Auto-generated method stub
		Claims claims = extractAllClaims(token);
		return claims.getSubject();
	}
	
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}
	
	public Long extractUserId(String token) {
		return extractAllClaims(token).get("userId", Long.class);	
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		Claims claims = extractAllClaims(token);
		return claims.getExpiration().before(new Date());

	}
	
	public Date getExpirationTime(String token) {
		return extractAllClaims(token).getExpiration();
	}
	
	
	// Generate jwt token
	public String genrateJwtToken(UserEntity user) {
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("userId", user.getId());
		claims.put("role", user.getRole());
		claims.put("type", "ACCESS");
		return buildToken(claims, user.getEmail(), expiration);
	}
	
	
	//Generate refresh token
	public String genrateRefreshToken(UserEntity user) {
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("type", "REFRESH");
		return buildToken(claims, user.getEmail(), refreshExpiration);
	}
	
	
	//Generate the token
	private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
		Instant now = Instant.now();
		
		return Jwts.builder()
				.claims(extraClaims)
				.subject(subject)
				.issuer("health-first")
				.id(UUID.randomUUID().toString())
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plusMillis(expiration)))
				.signWith(getSigningKey())
				.compact();
				
	}
	
	private SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
