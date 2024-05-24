package com.example.terabackendtest.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * LocalDateTime 관련 단위테스트를 위해 Clock을 Bean으로 등록
 */
@Configuration
public class TimeConfig {

	@Bean
	public Clock clock() {
		return Clock.systemDefaultZone();
	}
}
