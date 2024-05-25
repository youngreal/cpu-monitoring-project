package com.example.terabackendtest.repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.example.terabackendtest.repository.dto.CpuDtoForPerDaily;
import com.example.terabackendtest.repository.dto.CpuDtoForPerHour;
import com.example.terabackendtest.repository.dto.CpuDtoForPerMinute;

public interface CpuUsageRepositoryCustom {

	List<CpuDtoForPerMinute> findCpuUsagesPerMinute(final LocalDateTime startTime, final LocalDateTime endTime);

	List<CpuDtoForPerHour> findCpuUsagesPerHour(final LocalDateTime date, final LocalDateTime localDateTime);

	List<CpuDtoForPerDaily> findCpuUsagesPerDaily(final LocalDateTime startTime, final LocalDateTime endTime);
}
