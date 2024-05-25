package com.example.terabackendtest.controller.dto;

import com.example.terabackendtest.dto.CpuDto;

public record CpuUsagePerHourResponse(
	int hour,
	int minCpuUsage,
	int maxCpuUsage,
	double averageCpuUsage
) {

	public static CpuUsagePerHourResponse from(final CpuDto dto) {
		return new CpuUsagePerHourResponse(dto.hour(), dto.minCpuUsage(), dto.maxCpuUsage(), dto.averageCpuUsage());
	}
}
