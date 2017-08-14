package com.example.ssm.service.test;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheTestService {
	private int i = 0;

	@Cacheable("test")
	public int get() {
		return i;
	}

	public int add() {
		return ++i;
	}
	
	@Cacheable("unknown")
	public int getDefault() {
		return i;
	}

}
