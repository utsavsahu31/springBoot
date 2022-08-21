package com.productapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.productapp.repository.Product;

@Service
public class ScheduledJob {
	private Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

	@Autowired
	private ProductService service;

	@Scheduled(cron = "0,30 * * * * *")
	public void cronJob() {
		logger.info("> cronJob");

		List<Product> products = service.findAll();
		logger.info("There are {} products in the data store.", products.size());

		logger.info("< cronJob");
	}

	// after application startup delay of 5 sec, schedule to run each after 15
	
	@Scheduled(initialDelay = 5000, fixedRate = 15000)
	public void fixedRateJob() {
		logger.info("> fixedRateJob");

		// Add scheduled logic here

		List<Product> products = service.findAll();
		
		logger.info("There are {} books in the data store.", products.size());

		logger.info("< fixedRateJob");
	}

}

