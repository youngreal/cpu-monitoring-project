package com.example.terabackendtest.repository.dto;

public record CpuDtoForPerDaily(
	int minCpuUsage,
	int maxCpuUsage,
	double averageCpuUsage,
	int day
) {
	public static CpuDtoForPerDaily of(final int minCpuUsage, final int maxCpuUsage,
		final double averageCpuUsage, int day) {
		return new CpuDtoForPerDaily(minCpuUsage, maxCpuUsage, averageCpuUsage, day);
	}
}
