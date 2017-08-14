package com.example.ssm.module.cache;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCacheManager;

import com.google.common.cache.CacheBuilder;

public class ExpiresGuavaCacheManager extends GuavaCacheManager {
	private Properties cacheTime;
	private Properties cacheSize;

	private int expiry = 300;
	private int size = 1000;

	private Logger logger = Logger.getLogger(ExpiresGuavaCacheManager.class);

	public ExpiresGuavaCacheManager(Properties timeProperties, Properties sizeProperties) {
		super();
		this.cacheTime = timeProperties;
		this.cacheSize = sizeProperties;
		String expiryStr = cacheTime.getProperty("default");
		String sizeStr = cacheSize.getProperty("default");
		try {
			this.expiry = Integer.parseInt(expiryStr);
			this.size = Integer.parseInt(sizeStr);
			logger.info("Cache: default, expiry: " + expiry + ", size: " + size);
		} catch (Exception ex) {
			logger.warn("Cache: default, expiry: " + expiryStr + ", size: " + sizeStr, ex);
		}
	}

	public ExpiresGuavaCacheManager(int expiry, int size) {
		super();
		this.expiry = expiry;
		this.size = size;
		logger.info("Cache: default, expiry: " + expiry + ", size: " + size);
	}

	public ExpiresGuavaCacheManager() {
		super();
		logger.info("Cache: default, expiry: " + expiry + ", size: " + size);
	}

	@Override
	protected com.google.common.cache.Cache<Object, Object> createNativeGuavaCache(String name) {
		int expiry = this.expiry;
		int size = this.size;
		if (cacheTime != null) {
			String expiryStr = cacheTime.getProperty(name);
			try {
				expiry = Integer.parseInt(expiryStr);
			} catch (Exception ex) {
				logger.warn("Cache: " + name + ", expiry: " + expiryStr);
			}
		}
		if (cacheSize != null) {
			String sizeStr = cacheSize.getProperty(name);
			try {
				size = Integer.parseInt(sizeStr);
			} catch (Exception ex) {
				logger.warn("Cache: " + name + ", size: " + sizeStr);
			}
		}
		logger.info("Cache: " + name + ", expiry: " + expiry + ", size: " + size);
		com.google.common.cache.Cache<Object, Object> specCache = CacheBuilder.newBuilder()
				.expireAfterWrite(expiry, TimeUnit.SECONDS).maximumSize(size).build();
		return specCache;
	}

	@Override
	public Cache getCache(String name) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Cache Name: " + name);
		return super.getCache(name);
	}
	
}
