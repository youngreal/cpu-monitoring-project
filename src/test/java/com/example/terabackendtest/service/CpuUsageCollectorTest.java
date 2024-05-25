package com.example.terabackendtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.terabackendtest.exception.CpuUsageCollectFailException;
import com.sun.management.OperatingSystemMXBean;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CpuUsageCollectorTest {

	@InjectMocks
	private CpuUsageCollector sut;

	@Mock
	private OperatingSystemMXBean operatingSystemMXBean;

	@Test
	void Cpu_사용률_수집에_실패하면_예외를_던진다() {
		// given
		given(operatingSystemMXBean.getCpuLoad()).willReturn(-1.0);

		// when & then
		assertThrows(CpuUsageCollectFailException.class, () -> sut.usagePercent());
	}

	@Test
	void Cpu_사용률_수집에_성공하면_정수형으로_반환한다() {
		// given
		given(operatingSystemMXBean.getCpuLoad()).willReturn(0.3);

		// when
		final int result = sut.usagePercent();

		//then
		assertThat(result).isEqualTo(30);
	}
}