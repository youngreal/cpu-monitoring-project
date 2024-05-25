package com.example.terabackendtest.service;

import static com.example.terabackendtest.source.TestTimeSource.TEMPORARY_CPU_USAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.dto.CpuDto;
import com.example.terabackendtest.repository.CpuUsageRepository;
import com.example.terabackendtest.repository.dto.CpuDtoForPerDaily;
import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CpuUsageServiceTest {

	@InjectMocks
	private CpuUsageService sut;

	@Mock
	private CpuUsageRepository cpuUsageRepository;

	@Mock
	private CpuUsageCollector cpuUsageCollector;

	@Mock
	private TimeValidator timeValidator;

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
		final List<CpuDtoForPerMinute> mockCpuUsages = List.of(
			CpuDtoForPerMinute.of(10, LocalDateTime.now()),
			CpuDtoForPerMinute.of(40, LocalDateTime.now())
		);
		final List<CpuDto> expectedDtos = mockCpuUsages.stream()
			.map(CpuDto::from)
			.toList();
		given(cpuUsageRepository.findCpuUsagesPerMinute(startTime, endTime)).willReturn(mockCpuUsages);

		//when
		final List<CpuDto> result = sut.cpuUsagePerMinute(startTime, endTime);

		// then
		then(cpuUsageRepository).should().findCpuUsagesPerMinute(startTime, endTime);
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedDtos);
	}

	@Test
	void 시_단위의_CPU_사용률을_조회한다() {
		// given
		final LocalDate input = LocalDate.of(2024, 3, 25);
		final LocalDateTime startTime = LocalDateTime.of(2024, 3, 25, 0, 0);
		final List<CpuDtoForPerHour> mockCpuUsages = List.of(
			CpuDtoForPerHour.of(10, 20, 40.0, 5),
			CpuDtoForPerHour.of(40, 50, 70.0, 6)
		);
		final List<CpuDto> expectedDtos = mockCpuUsages.stream()
			.map(CpuDto::from)
			.toList();
		given(cpuUsageRepository.findCpuUsagesPerHour(startTime, startTime.plusDays(1))).willReturn(mockCpuUsages);

		//when
		final List<CpuDto> result = sut.cpuUsagePerHour(input);

		// then
		then(cpuUsageRepository).should().findCpuUsagesPerHour(startTime, startTime.plusDays(1));
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedDtos);
	}

	@Test
	void 일_단위의_CPU_사용률을_조회한다() {
		// given
		final LocalDate startDate = LocalDate.of(2024, 3, 25);
		final LocalDate endDate = LocalDate.of(2024, 5, 22);
		final List<CpuDtoForPerDaily> mockCpuUsages = List.of(
			CpuDtoForPerDaily.of(10, 20, 40.0, 5),
			CpuDtoForPerDaily.of(40, 50, 70.0, 6)
		);
		final List<CpuDto> expectedDtos = mockCpuUsages.stream()
			.map(CpuDto::from)
			.toList();
		given(cpuUsageRepository.findCpuUsagesPerDaily(startDate.atStartOfDay(), endDate.atStartOfDay())).willReturn(mockCpuUsages);

		// when
		final List<CpuDto> result = sut.cpuUsagePerDaily(startDate, endDate);

		// then
		then(cpuUsageRepository).should().findCpuUsagesPerDaily(startDate.atStartOfDay(), endDate.atStartOfDay());
		assertThat(result).usingRecursiveComparison().isEqualTo(expectedDtos);
	}
}