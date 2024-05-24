package com.example.terabackendtest.service;

import static com.example.terabackendtest.source.TestTimeSource.TEMPORARY_CPU_USAGE;
import static org.mockito.ArgumentMatchers.any;
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
		given(cpuUsageCollector.usagePercent()).willReturn(TEMPORARY_CPU_USAGE);

		// when
		sut.saveCpuUsage();

		// then
		then(cpuUsageRepository).should().save(any(CpuUsage.class));
	}

	@Test
	void 분_단위의_CPU_사용률을_조회한다() {
		// given
		final LocalDateTime startTime = LocalDateTime.of(2024, 5, 18, 1, 0);
		final LocalDateTime endTime = LocalDateTime.of(2024, 5, 22, 8, 0);

		//when
		sut.cpuUsageBetween(startTime, endTime);

		// then
		then(cpuUsageRepository).should().findCpuUsageBetween(startTime, endTime);
	}
}