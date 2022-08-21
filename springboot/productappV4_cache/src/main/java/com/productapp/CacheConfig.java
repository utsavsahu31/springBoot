package com.productapp;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//1. configure cache
@Configuration
@EnableCaching
public class CacheConfig {
	@Bean
	public CacheManager cacheManager(){
		ConcurrentMapCacheManager cacheManager=new ConcurrentMapCacheManager("products");//in memory cache
		return cacheManager;
		
	}
}
