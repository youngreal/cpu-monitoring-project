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
import com.example.terabackendtest.dto.CpuDto;

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
		final List<CpuDto> result = sut.findCpuUsageBetween(startTime, endTime);

		// then
		final List<CpuDto> cpuDtos = List.of(
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 23, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 24, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 25, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 17, 26, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 4, 17, 23, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 5, 17, 23, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 6, 17, 23, 0)),
			CpuDto.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 7, 17, 23, 0))
		);
		assertThat(result).isEqualTo(cpuDtos);
	}

}
