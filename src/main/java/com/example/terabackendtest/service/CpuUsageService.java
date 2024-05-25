package com.example.terabackendtest.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.dto.CpuDto;
import com.example.terabackendtest.repository.CpuUsageRepository;

@Service
public class CpuUsageService {

	private static final int ONE_MINUTE = 60_000;
	private final CpuUsageRepository cpuUsageRepository;
	private final CpuUsageCollector cpuUsageCollector;

	public CpuUsageService(final CpuUsageRepository cpuUsageRepository, final CpuUsageCollector cpuUsageCollector) {
		this.cpuUsageRepository = cpuUsageRepository;
		this.cpuUsageCollector = cpuUsageCollector;
	}

	@Scheduled(fixedDelay = ONE_MINUTE)
	public void saveCpuUsage() {
		final int cpuUsagePercent = cpuUsageCollector.usagePercent();
		cpuUsageRepository.save(CpuUsage.of(cpuUsagePercent, LocalDateTime.now()));
	}

	@Transactional(readOnly = true)
	public List<CpuDto> cpuUsageBetween(final LocalDateTime startTime, final LocalDateTime endTime) {
		return cpuUsageRepository.findCpuUsageBetween(startTime, endTime);
	}
}
