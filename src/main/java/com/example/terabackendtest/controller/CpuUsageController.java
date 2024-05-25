package com.example.terabackendtest.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.terabackendtest.controller.dto.CpuUsagePerDailyResponse;
import com.example.terabackendtest.controller.dto.CpuUsagePerHourResponse;
import com.example.terabackendtest.controller.swagger.CpuUsageSwagger;
import com.example.terabackendtest.controller.dto.CpuUsageResponse;
import com.example.terabackendtest.service.CpuUsageService;

@RestController
public class CpuUsageController implements CpuUsageSwagger {

	private final CpuUsageService cpuUsageService;

	public CpuUsageController(final CpuUsageService cpuUsageService) {
		this.cpuUsageService = cpuUsageService;
	}

	@GetMapping("/cpu-usages/minute")
	public List<CpuUsageResponse> cpuUsages(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime endTime
	) {
		return cpuUsageService.cpuUsagePerMinute(startTime, endTime).stream()
			.map(CpuUsageResponse::from)
			.toList();
	}

	@GetMapping("/cpu-usages/hourly")
	public List<CpuUsagePerHourResponse> cpuUsagesForHourly(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date) {
		return cpuUsageService.cpuUsagePerHour(date).stream()
			.map(CpuUsagePerHourResponse::from)
			.toList();
	}

	@GetMapping("/cpu-usages/daily")
	public List<CpuUsagePerDailyResponse> cpuUsagesForDaily(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate
	) {
		return cpuUsageService.cpuUsagePerDaily(startDate, endDate).stream()
			.map(CpuUsagePerDailyResponse::from)
			.toList();
	}
}
