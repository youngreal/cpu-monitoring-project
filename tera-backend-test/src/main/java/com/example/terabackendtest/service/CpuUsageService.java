package com.example.terabackendtest.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.terabackendtest.domain.CpuUsage;
import com.example.terabackendtest.repository.CpuUsageRepository;
import com.example.terabackendtest.util.CpuUsageCollector;

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
}
