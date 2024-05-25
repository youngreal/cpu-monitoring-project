package com.example.terabackendtest.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.terabackendtest.dto.CpuDto;
import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;

public interface CpuUsageRepositoryCustom {

	List<CpuDtoForPerMinute> findCpuUsageBetween(final LocalDateTime startTime, final LocalDateTime endTime);

	List<CpuDtoForPerHour> findCpuUsagesForDay(final LocalDateTime date, final LocalDateTime localDateTime);
}
