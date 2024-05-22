package com.example.terabackendtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.repository.CpuUsageRepository;
import com.example.terabackendtest.util.CpuUsageCollector;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CpuUsageServiceTest {

	@InjectMocks
	private CpuUsageService sut;

	@Mock
	private CpuUsageRepository cpuUsageRepository;

	@Mock
	private CpuUsageCollector cpuUsageCollector;

	@Test
	void CPU_사용률을_수집후_저장한다() {
		// given
		given(cpuUsageCollector.usagePercent()).willReturn(50);
		CpuUsage cpuUsage = CpuUsage.of(cpuUsageCollector.usagePercent(), LocalDateTime.now());

		// when
		sut.saveCpuUsage();

		// then
		then(cpuUsageRepository.save(cpuUsage));
	}
}