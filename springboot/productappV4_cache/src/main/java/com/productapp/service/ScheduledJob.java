package com.productapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledJob {
	
	@Autowired
	private ProductService productService;
	
	private Logger logger=LoggerFactory.getLogger(ScheduledJob.class);

	@Scheduled(initialDelay = 6000, fixedRate = 5000)
	public void fixedRateMethod() {
		productService.evictCache();
		logger.info("cached is clean again!");
	}
	
}
