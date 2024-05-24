package com.example.terabackendtest.config;

import java.lang.management.ManagementFactory;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sun.management.OperatingSystemMXBean;

/**
 * OperatingSystemMXBean를 Mocking하여 단위테스트를 진행하기위해 빈 등록
 */
@Configuration
@EnableAutoConfiguration(exclude = {
	JmxAutoConfiguration.class
})
public class MonitoringConfig {

	@Bean
	public OperatingSystemMXBean operatingSystemMXBean() {
		return ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
	}
}
