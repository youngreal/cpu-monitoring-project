package com.example.terabackendtest.controller.dto;

import java.time.LocalDateTime;

import com.example.terabackendtest.dto.CpuDto;

public record CpuUsageResponse(
	int usagePercent,
	LocalDateTime timestamp
) {

	public static CpuUsageResponse from(final CpuDto cpuDto) {
		return new CpuUsageResponse(cpuDto.usagePercent(), cpuDto.timestamp());
	}
}
