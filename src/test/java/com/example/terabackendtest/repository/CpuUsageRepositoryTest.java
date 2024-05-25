package com.example.terabackendtest.repository;

import static com.example.terabackendtest.source.TestTimeSource.TEMPORARY_CPU_USAGE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import(TestConfig.class)
@DataJpaTest
class CpuUsageRepositoryTest {

	@Autowired
	private CpuUsageRepository sut;

	@Test
	void Querydsl_지정한_시간구간의_분단위_Cpu_사용률을_조회한다() {
		// given
		final var cpuUsages = List.of(
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 1, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 2, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 24, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 25, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 26, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 4, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 5, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 6, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 7, 17, 23, 0)
			));

		sut.saveAll(cpuUsages);

		// when
		final var startTime = LocalDateTime.of(2024, 5, 3, 17, 23, 0);
		final var endTime = LocalDateTime.of(2024, 5, 7, 17, 23, 0);
		final List<CpuDtoForPerMinute> result = sut.findCpuUsageBetween(startTime, endTime);

		// then
		final List<CpuDtoForPerMinute> cpuDtos = List.of(
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 23, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 24, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 25, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 26, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 4, 17, 23, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 5, 17, 23, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 6, 17, 23, 0)),
			CpuDtoForPerMinute.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 7, 17, 23, 0))
		);
		assertThat(result).isEqualTo(cpuDtos);
	}

	@Test
	void Querydsl_지정한_날짜구간의_시단위_Cpu_사용률을_조회한다() {
		// given
		final var cpuUsages = List.of(
			CpuUsage.of(10, LocalDateTime.of(2024, 5, 20, 17, 0, 0)),
			CpuUsage.of(20, LocalDateTime.of(2024, 5, 20, 17, 10, 0)),
			CpuUsage.of(30, LocalDateTime.of(2024, 5, 20, 17, 20, 0)),
			CpuUsage.of(40, LocalDateTime.of(2024, 5, 20, 18, 0, 0)),
			CpuUsage.of(50, LocalDateTime.of(2024, 5, 20, 18, 25, 0)),
			CpuUsage.of(60, LocalDateTime.of(2024, 5, 20, 19, 26, 0)),
			CpuUsage.of(70, LocalDateTime.of(2024, 5, 20, 19, 23, 0)),
			CpuUsage.of(80, LocalDateTime.of(2024, 5, 20, 20, 23, 0)),
			CpuUsage.of(90, LocalDateTime.of(2024, 5, 20, 20, 23, 0)),
			CpuUsage.of(100, LocalDateTime.of(2024, 5, 20, 23, 23, 0)
			));

		sut.saveAll(cpuUsages);

		// when
		final var startTime = LocalDateTime.of(2024, 5, 20, 17, 0, 0);
		final List<CpuDtoForPerHour> result = sut.findCpuUsagesForDay(startTime, startTime.plusDays(1));

		// then
		final List<CpuDtoForPerHour> cpuDtos = List.of(
			CpuDtoForPerHour.of(10, 30, 20.0, 17),
			CpuDtoForPerHour.of(40, 50, 45.0, 18),
			CpuDtoForPerHour.of(60, 70, 65.0, 19),
			CpuDtoForPerHour.of(80, 90, 85.0, 20),
			CpuDtoForPerHour.of(100, 100, 100.0, 23)
		);
		assertThat(result).isEqualTo(cpuDtos);
	}

}
