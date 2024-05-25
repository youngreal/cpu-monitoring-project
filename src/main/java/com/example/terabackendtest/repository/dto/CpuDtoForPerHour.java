package com.example.terabackendtest.repository.dto;

public record CpuDtoForPerHour(
	int minCpuUsage,
	int maxCpuUsage,
	double averageCpuUsage,
	int hour
) {

	public static CpuDtoForPerHour of(final int minCpuUsage, final int maxCpuUsage,
		final double averageCpuUsage, int hour) {
		return new CpuDtoForPerHour(minCpuUsage, maxCpuUsage, averageCpuUsage, hour);
	}
}
