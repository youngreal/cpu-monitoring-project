package com.example.terabackendtest.repository.dto;

import java.time.LocalDateTime;

public record CpuDtoForPerMinute(
	int usagePercent,
	LocalDateTime timestamp
) {
	public static CpuDtoForPerMinute of(final int usagePercent, final LocalDateTime timestamp) {
		return new CpuDtoForPerMinute(usagePercent, timestamp);
	}
}
