package com.example.terabackendtest.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.terabackendtest.dto.CpuDto;

public interface CpuUsageRepositoryCustom {

	List<CpuDto> findCpuUsageBetween(final LocalDateTime startTime, final LocalDateTime endTime);
}
