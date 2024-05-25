package com.example.terabackendtest.dto;

import java.time.LocalDateTime;

public record CpuDto(
	int usagePercent,
	LocalDateTime timestamp
) {

	public static CpuDto of(final int usagePercent, final LocalDateTime timestamp) {
		return new CpuDto(usagePercent, timestamp);
	}
}
