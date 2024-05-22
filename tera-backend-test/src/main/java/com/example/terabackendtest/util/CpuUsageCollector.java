package com.example.terabackendtest.util;

import java.lang.management.ManagementFactory;

import org.springframework.stereotype.Component;

import com.sun.management.OperatingSystemMXBean;

@Component
public class CpuUsageCollector {

	private static final int PERCENTAGE = 100;
	private static final OperatingSystemMXBean bean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

	public int usagePercent() {
		return (int)(bean.getCpuLoad() * PERCENTAGE);
	}
}
