package com.example.terabackendtest.service;

import java.lang.management.ManagementFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.terabackendtest.exception.CpuUsageCollectFailException;
import com.sun.management.OperatingSystemMXBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class CpuUsageCollector {

	private static final int PERCENTAGE = 100;
	private static final int LOWEST_CPU_USAGE_RATE = 0;
	private final OperatingSystemMXBean operatingSystemMXBean;

	public int usagePercent() {
		final double cpuUsage = operatingSystemMXBean.getCpuLoad();
		if (cpuUsage < LOWEST_CPU_USAGE_RATE) {
			log.error("cpu collection failed");
			throw new CpuUsageCollectFailException();
		}
		return (int)(operatingSystemMXBean.getCpuLoad() * PERCENTAGE);
	}
}
