package com.healthfirst.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
	
	private final StringRedisTemplate stringRedisTemplate;
	
	public void blackListToken(String token, long remainingExpireMilliSeconds) {
		if (remainingExpireMilliSeconds> 0) {
			stringRedisTemplate.opsForValue().set("blacklist:" + token, "revoked", remainingExpireMilliSeconds, TimeUnit.MILLISECONDS);
		}
	}
	
	public boolean isTokenBlacklisted(String token) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey("blacklist:" + token));
	}
}
