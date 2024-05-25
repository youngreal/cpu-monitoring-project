package com.example.terabackendtest.controller;

import static com.example.terabackendtest.source.TestTimeSource.TEMPORARY_CPU_USAGE;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.repository.CpuUsageRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
class CpuUsageControllerTest {

	private static final String CPU_USAGE_ENDPOINT_URL = "/cpu-usages";
	/**
	 * 테스트용 현재 시간을 위해 2024.05.22T15:00:00Z로 시간고정
	 * UTC 시간차이로 인해 9시간 느리게 적용됩니다(실제 프로덕션 코드에서 적용되는 LocalDateTime 기준으로 해당값은 2024.05.23T00:00:00로 적용됩니다)
	 */
	private static final String TEMPORARY_CURRENT_TIME = "2024-05-22T15:00:00Z";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CpuUsageRepository cpuUsageRepository;

	@SpyBean
	private Clock clock;

	@BeforeEach
	void setUp() {
		given(clock.instant()).willReturn(Instant.parse(TEMPORARY_CURRENT_TIME));

		final List<CpuUsage> cpuUsages = List.of(
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 1, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 2, 17, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 1, 23, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 3, 1, 24, 0)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 20, 9, 23, 1)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 20, 9, 24, 2)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 20, 9, 25, 2)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 20, 9, 26, 2)),
			CpuUsage.of(TEMPORARY_CPU_USAGE, LocalDateTime.of(2024, 5, 23, 17, 23, 0)
			));

		cpuUsageRepository.saveAll(cpuUsages);
	}

	@Test
	void 일주일_내의_Cpu_사용률을_조회한다() throws Exception {
		// when & then
		mockMvc.perform(get(CPU_USAGE_ENDPOINT_URL)
				.param("startTime", "2024-05-18T01:00")
				.param("endTime", "2024-05-22T08:00")
			)
			.andDo(print())
			.andExpect(jsonPath("$.cpuDto[0].usagePercent").value(TEMPORARY_CPU_USAGE))
			.andExpect(jsonPath("$.cpuDto[0].timestamp").value("2024-05-20T09:23:01"))
			.andExpect(jsonPath("$.cpuDto[1].usagePercent").value(TEMPORARY_CPU_USAGE))
			.andExpect(jsonPath("$.cpuDto[1].timestamp").value("2024-05-20T09:24:02"))
			.andExpect(jsonPath("$.cpuDto[2].usagePercent").value(TEMPORARY_CPU_USAGE))
			.andExpect(jsonPath("$.cpuDto[2].timestamp").value("2024-05-20T09:25:02"))
			.andExpect(jsonPath("$.cpuDto[3].usagePercent").value(TEMPORARY_CPU_USAGE))
			.andExpect(jsonPath("$.cpuDto[3].timestamp").value("2024-05-20T09:26:02"))
			.andExpect(status().isOk());
	}

	@MethodSource
	@ParameterizedTest
	void 부적절한_입력으로_Cpu_사용률_조회에_실패한다(final String startTime, final String endTime) throws Exception {
		// when & then
		mockMvc.perform(get(CPU_USAGE_ENDPOINT_URL)
				.param("startTime", startTime)
				.param("endTime", endTime)
			)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	static Stream<Arguments> 부적절한_입력으로_Cpu_사용률_조회에_실패한다() {
		return Stream.of(
			arguments("2024-05-15T01:00", "2024-05-15T08:00"), // 끝시간이 1주일 이전인 경우
			arguments("2024-05-22T01:00", "2024-05-21T01:00"), // startTime이 endTime보다 더 늦은 시간인 경우
			arguments("2024-05-31T01:00","2024-05-31T05:00"), // startTime이 현재 시간보다 미래인경우
			arguments("2024-05-22T01:00","2024-05-28T01:00") //endTime이 현재 시간보다 미래인경우
		);
	}

	@MethodSource
	@ParameterizedTest
	void 입력_날짜_형식이_잘못되면_조회에_실패한다(final String startTime, final String endTime) throws Exception {
		// when & then
		mockMvc.perform(get(CPU_USAGE_ENDPOINT_URL)
				.param("startTime", startTime)
				.param("endTime", endTime)
			)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	static Stream<Arguments> 입력_날짜_형식이_잘못되면_조회에_실패한다() {
		return Stream.of(
			arguments("", "2024-05-15T08:00"),
			arguments(" ", "2024-05-21T01:00"),
			arguments("??asdsadsd","2024-05-31T05:00"),
			arguments("2024.05.28","2024-05-28T01:00")
		);
	}
}