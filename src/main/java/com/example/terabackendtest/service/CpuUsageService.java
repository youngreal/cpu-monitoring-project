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
	private final TimeValidator timeValidator;
	private final CpuUsageRepository cpuUsageRepository;
	private final CpuUsageCollector cpuUsageCollector;

	public CpuUsageService(final TimeValidator timeValidator, final CpuUsageRepository cpuUsageRepository, final CpuUsageCollector cpuUsageCollector) {
		this.timeValidator = timeValidator;
		this.cpuUsageRepository = cpuUsageRepository;
		this.cpuUsageCollector = cpuUsageCollector;
	}

	@Scheduled(fixedDelay = ONE_MINUTE)
	public void saveCpuUsage() {
		final int cpuUsagePercent = cpuUsageCollector.usagePercent();
		cpuUsageRepository.save(CpuUsage.of(cpuUsagePercent, LocalDateTime.now()));
	}

	@Transactional(readOnly = true)
	public List<CpuDto> cpuUsagePerMinute(final LocalDateTime startTime, final LocalDateTime endTime) {
		timeValidator.validateDateRangeForPerMinute(startTime, endTime);

		return cpuUsageRepository.findCpuUsagesPerMinute(startTime, endTime).stream()
			.map(CpuDto::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<CpuDto> cpuUsagePerHour(final LocalDate date) {
		// validation
		timeValidator.validateDateRangeForPerHour(date);

		final LocalDateTime startTime = date.atStartOfDay();
		return cpuUsageRepository.findCpuUsagesPerHour(startTime, startTime.plusDays(1)).stream()
			.map(CpuDto::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<CpuDto> cpuUsagePerDaily(final LocalDate startDate, final LocalDate endDate) {
		// validation
		timeValidator.validateDateRangeForPerDaily(startDate, endDate);

		return cpuUsageRepository.findCpuUsagesPerDaily(startDate.atStartOfDay(), endDate.atStartOfDay()).stream()
			.map(CpuDto::from)
			.toList();
	}
}
