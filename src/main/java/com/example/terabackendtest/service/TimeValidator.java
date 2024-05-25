package com.example.terabackendtest.service;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.example.terabackendtest.exception.InvalidDateRangeException;

@Component
public class TimeValidator {

	private final Clock clock;

	public TimeValidator(final Clock clock) {
		this.clock = clock;
	}

	public void validateDateRangeForPerDaily(final LocalDate startDate, final LocalDate endDate) {
		final LocalDate currentTime = LocalDate.now(clock);
		if (startDate.isBefore(currentTime.minusYears(1)) || startDate.isAfter(currentTime) || endDate.isAfter(
			currentTime) || startDate.isAfter(endDate)) {
			throw new InvalidDateRangeException();
		}
	}

	public void validateDateRangeForPerHour(final LocalDate date) {
		final LocalDate currentTime = LocalDate.now(clock);
		if (date.isBefore(currentTime.minusMonths(3)) || date.isAfter(currentTime)) {
			throw new InvalidDateRangeException();
		}
	}
}
