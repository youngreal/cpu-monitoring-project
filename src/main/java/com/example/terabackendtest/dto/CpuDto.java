package com.example.terabackendtest.dto;

import java.time.LocalDateTime;

import com.example.terabackendtest.repository.dto.CpuDtoForPerDaily;
import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;

import lombok.Builder;

@Builder
public record CpuDto(
	int usagePercent,
	LocalDateTime timestamp,
	int minCpuUsage,
	int maxCpuUsage,
	double averageCpuUsage,
	int hour,
	int day
) {

	public static CpuDto of(final int usagePercent, final LocalDateTime timestamp) {
		return CpuDto.builder()
			.usagePercent(usagePercent)
			.timestamp(timestamp)
			.build();
	}

	public static CpuDto from(final CpuDtoForPerHour cpuDtoForPerHour) {
		return CpuDto.builder()
			.minCpuUsage(cpuDtoForPerHour.minCpuUsage())
			.maxCpuUsage(cpuDtoForPerHour.maxCpuUsage())
			.averageCpuUsage(cpuDtoForPerHour.averageCpuUsage())
			.hour(cpuDtoForPerHour.hour())
			.build();
	}

	public static CpuDto from(final CpuDtoForPerMinute cpuDtoForPerMinute) {
		return CpuDto.builder()
			.usagePercent(cpuDtoForPerMinute.usagePercent())
			.timestamp(cpuDtoForPerMinute.timestamp())
			.build();
	}

	public static CpuDto from(final CpuDtoForPerDaily cpuDtoForPerDaily) {
		return CpuDto.builder()
			.minCpuUsage(cpuDtoForPerDaily.minCpuUsage())
			.maxCpuUsage(cpuDtoForPerDaily.maxCpuUsage())
			.averageCpuUsage(cpuDtoForPerDaily.averageCpuUsage())
			.day(cpuDtoForPerDaily.day())
			.build();
	}
}
