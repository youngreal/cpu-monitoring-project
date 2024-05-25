package com.example.terabackendtest.controller.dto;

import java.util.List;

import com.example.terabackendtest.dto.CpuDto;

public record CpuUsageResponse(
	List<CpuDto> cpuDto
) {

	public static CpuUsageResponse from(List<CpuDto> cpuDtos) {
		return new CpuUsageResponse(cpuDtos);
	}
}
