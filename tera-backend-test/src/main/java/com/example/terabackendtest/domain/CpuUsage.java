package com.example.terabackendtest.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CpuUsage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private int usagePercent; // CPU 사용률

	private LocalDateTime timestamp; // 수집 시간

	protected CpuUsage() {
	}

	private CpuUsage(final int usagePercent, final LocalDateTime timestamp) {
		this.usagePercent = usagePercent;
		this.timestamp = timestamp;
	}

	public static CpuUsage of(final int usagePercent, final LocalDateTime timestamp) {
		return new CpuUsage(usagePercent, timestamp);
	}
}
