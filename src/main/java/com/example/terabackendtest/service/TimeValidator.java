package com.example.terabackendtest.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.terabackendtest.exception.InvalidDateRangeException;

import lombok.extern.slf4j.Slf4j;

@Component
public class TimeValidator {

	private final Clock clock;

	public TimeValidator(final Clock clock) {
		this.clock = clock;
	}

	public LocalDateTime startTimeForCpuUsagePerHour(final LocalDate date) {
		final LocalDate currentTime = LocalDate.now(clock);
		if (date.isBefore(currentTime.minusMonths(3)) || date.isAfter(currentTime)) {
			throw new InvalidDateRangeException();
		}
		return date.atStartOfDay();
	}
}
