package com.essexboy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MetricsServiceTests {

	@Autowired
	MetricsService metricsService;

	@Test
	void contextLoads() throws InterruptedException {
		assertNotNull(metricsService);
		Thread.sleep(5000L);
		System.out.println(metricsService.getFullMetricsMap());
	}
}
