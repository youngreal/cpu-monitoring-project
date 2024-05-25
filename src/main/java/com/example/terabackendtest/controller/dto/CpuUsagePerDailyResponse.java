package com.example.terabackendtest.controller.dto;

import com.example.terabackendtest.dto.CpuDto;

public record CpuUsagePerDailyResponse(
	int day,
	int minCpuUsage,
	int maxCpuUsage,
	double averageCpuUsage
) {

	public static CpuUsagePerDailyResponse from(final CpuDto dto) {
		return new CpuUsagePerDailyResponse(dto.day(), dto.minCpuUsage(), dto.maxCpuUsage(), dto.averageCpuUsage());
	}
}
