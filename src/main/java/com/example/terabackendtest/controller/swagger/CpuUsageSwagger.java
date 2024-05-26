package com.example.terabackendtest.controller.swagger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.terabackendtest.controller.dto.CpuUsagePerDailyResponse;
import com.example.terabackendtest.controller.dto.CpuUsagePerHourResponse;
import com.example.terabackendtest.controller.dto.CpuUsageResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "cpuUsages", description = "Cpu사용률 API")
public interface CpuUsageSwagger {

	@Operation(summary = "분 단위 CPU 사용률을 조회한다.",
	responses = {
		@ApiResponse(responseCode = "200", description = "정상적으로 분 단위 CPU 사용률 조회"),
		@ApiResponse(responseCode = "400", description = "입력날짜의 범위 검증에 실패했거나, 형식이 어긋난 경우 ",
			content = {
				@Content(mediaType = "application/json",
					examples = {
						@ExampleObject(name = "MethodArgumentTypeMismatchException", value = "{\"message\": \"잘못된 입력형식 입니다\"}"),
						@ExampleObject(name = "InvalidDateTimeRangeException", value = "{\"message\": \"%s is invalid datetime range\"}"),
						@ExampleObject(name = "StartTimeAfterEndTimeException", value = "{\"message\": \"start time cannot exceed the end time\"}")
					})
			})
	})
	List<CpuUsageResponse> cpuUsages(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@Parameter(description = "시작 시간, ISO DateTime(yyyy-MM-ddTHH:MM:SS) 형식으로 입력")
		final LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		@Parameter(description = "끝 시간, ISO DateTime(yyyy-MM-ddTHH:MM:SS) 형식으로 입력")
		final LocalDateTime endTime
	);

	@Operation(summary = "시 단위 CPU 사용률을 조회한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "정상적으로 시 단위 CPU 사용률 조회"),
			@ApiResponse(responseCode = "400", description = "입력날짜의 범위 검증에 실패했거나, 형식이 어긋난 경우 ",
				content = {
					@Content(mediaType = "application/json",
						examples = {
							@ExampleObject(name = "MethodArgumentTypeMismatchException", value = "{\"message\": \"잘못된 입력형식 입니다\"}"),
							@ExampleObject(name = "InvalidDateRangeException", value = "{\"message\": \"%s is invalid date range\"}")
						})
				})
		})
	List<CpuUsagePerHourResponse> cpuUsagesForHourly(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@Parameter(description = "시작 날짜, ISO Date(yyyy-MM-dd) 형식으로 입력")
		final LocalDate date
	);

	@Operation(summary = "일 단위 CPU 사용률을 조회한다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "정상적으로 일 단위 CPU 사용률 조회"),
			@ApiResponse(responseCode = "400", description = "입력날짜의 범위 검증에 실패했거나, 형식이 어긋난 경우 ",
				content = {
					@Content(mediaType = "application/json",
						examples = {
							@ExampleObject(name = "MethodArgumentTypeMismatchException", value = "{\"message\": \"잘못된 입력형식 입니다\"}"),
							@ExampleObject(name = "InvalidDateRangeException", value = "{\"message\": \"%s is invalid date range\"}")
						})
				})
		})
	List<CpuUsagePerDailyResponse> cpuUsagesForDaily(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@Parameter(description = "시작 날짜,ISO Date(yyyy-MM-dd) 형식으로 입력")
		final LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		@Parameter(description = "끝 날짜,ISO Date(yyyy-MM-dd) 형식으로 입력")
		final LocalDate endDate
	);

}