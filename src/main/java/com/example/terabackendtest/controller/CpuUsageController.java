package com.example.terabackendtest.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.terabackendtest.controller.validation.CheckEndTime;
import com.example.terabackendtest.controller.validation.CheckStartTime;
import com.example.terabackendtest.controller.dto.CpuUsageResponse;
import com.example.terabackendtest.exception.StartTimeAfterEndTimeException;
import com.example.terabackendtest.service.CpuUsageService;

@Validated
@RestController
public class CpuUsageController {

	private final CpuUsageService cpuUsageService;

	public CpuUsageController(final CpuUsageService cpuUsageService) {
		this.cpuUsageService = cpuUsageService;
	}

	@GetMapping("/cpu-usages")
	public CpuUsageResponse cpuUsages(
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @CheckStartTime final LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @CheckEndTime final LocalDateTime endTime
	) {
		if (startTime.isAfter(endTime)) {
			throw new StartTimeAfterEndTimeException();
		}
		return CpuUsageResponse.from(cpuUsageService.cpuUsageBetween(startTime, endTime));
	}
}
