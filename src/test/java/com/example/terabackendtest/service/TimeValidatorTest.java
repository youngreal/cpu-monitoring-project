package com.example.terabackendtest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.terabackendtest.exception.InvalidDateRangeException;
import com.example.terabackendtest.exception.InvalidDateTimeRangeException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TimeValidatorTest {

	/**
	 * 테스트용 현재 시간을 위해 2024.05.22T15:00:00Z로 시간고정
	 * UTC 시간차이로 인해 9시간 느리게 적용됩니다(실제 프로덕션 코드에서 적용되는 LocalDateTime 기준으로 해당값은 2024.05.23T00:00:00로 적용됩니다)
	 */
	private static final String TEMPORARY_CURRENT_TIME = "2024-05-22T15:00:00Z";

	@InjectMocks
	private TimeValidator sut;

	@Mock
	private Clock clock;

	@BeforeEach
	void setup() {
		// TEMPORARY_CURRENT_TIME에 설정된 시간으로 Clock 인스턴스를 고정합니다.
		final Instant fixedInstant = Instant.parse(TEMPORARY_CURRENT_TIME);
		final ZoneId systemDefaultZone = ZoneId.systemDefault();
		final Clock fixedClock = Clock.fixed(fixedInstant, systemDefaultZone);

		// 테스트 대상인 TimeValidator 내부에서 사용하는 Clock을 고정된 Clock으로 교체
		when(clock.instant()).thenReturn(fixedClock.instant());
		when(clock.getZone()).thenReturn(fixedClock.getZone());
	}

	@MethodSource
	@ParameterizedTest
	void 부적절한_입력으로_분_단위_Cpu_사용률_조회에_실패한다(final LocalDateTime startTime, final LocalDateTime endTime) {
		// when & then
		assertThrows(InvalidDateTimeRangeException.class, () -> sut.validateDateRangeForPerMinute(startTime, endTime));

	}

	static Stream<Arguments> 부적절한_입력으로_분_단위_Cpu_사용률_조회에_실패한다() {
		return Stream.of(
			arguments(LocalDateTime.of(2023, 5, 15,1,0), LocalDateTime.of(2024, 5, 15,8,0)), // 끝시간이 1주일 이전인 경우
			arguments(LocalDateTime.of(2023, 5, 22,1,0), LocalDateTime.of(2024, 5, 21,1,0)), // startTime이 endTime보다 더 늦은 시간인 경우
			arguments(LocalDateTime.of(2023, 5, 31,1,0), LocalDateTime.of(2024, 5, 31,5,0)), // startTime이 현재 시간보다 미래인경우
			arguments(LocalDateTime.of(2023, 5, 22,1,0), LocalDateTime.of(2024, 5, 28,1,0)) //endTime이 현재 시간보다 미래인경우
		);
	}


	@MethodSource
	@ParameterizedTest
	void 부적절한_입력으로_시_단위_Cpu_사용률_조회에_실패한다(final LocalDate input) {
		// when & then
		assertThrows(InvalidDateRangeException.class, () -> sut.validateDateRangeForPerHour(input));
	}

	static Stream<Arguments> 부적절한_입력으로_시_단위_Cpu_사용률_조회에_실패한다() {
		return Stream.of(
			arguments(LocalDate.of(2024, 2, 22)), // 입력 시간이 3개월보다 오래된경우
			arguments(LocalDate.of(2024, 5, 25)) // 입력 시간이 현재시간보다 미래인경우
		);
	}

	@MethodSource
	@ParameterizedTest
	void 부적절한_입력으로_일_단위_Cpu_사용률_조회에_실패한다(final LocalDate startTime, final LocalDate endTime) {
		// when & then
		assertThrows(InvalidDateRangeException.class, () -> sut.validateDateRangeForPerDaily(startTime, endTime));
	}

	static Stream<Arguments> 부적절한_입력으로_일_단위_Cpu_사용률_조회에_실패한다() {
		return Stream.of(
			arguments(LocalDate.of(2023, 5, 22), LocalDate.of(2024, 5, 23)), // 시작 시간이 1년보다 오래된경우
			arguments(LocalDate.of(2024, 5, 25), LocalDate.of(2024, 5, 25)), // 시작 시간이 현재시간보다 미래인경우
			arguments(LocalDate.of(2024, 5, 22), LocalDate.of(2024, 6, 22)), // 끝 시간이 현재시간보다 미래인경우
			arguments(LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 18)) // 시작 시간이 끝시간보다 미래인경우
		);
	}
}