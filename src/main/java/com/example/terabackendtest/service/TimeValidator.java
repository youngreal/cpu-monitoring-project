package com.example.terabackendtest.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.terabackendtest.exception.InvalidDateRangeException;
import com.example.terabackendtest.exception.InvalidDateTimeRangeException;
import com.example.terabackendtest.exception.StartTimeAfterEndTimeException;

@Component
public class TimeValidator {

	private final Clock clock;

	public TimeValidator(final Clock clock) {
		this.clock = clock;
	}

	public void validateDateRangeForPerDaily(final LocalDate startDate, final LocalDate endDate) {
		final LocalDate currentTime = LocalDate.now(clock);
		if (startDate.isBefore(currentTime.minusYears(1)) || startDate.isAfter(currentTime) || startDate.isAfter(
			endDate)) {
			throw new InvalidDateRangeException(startDate);
		}

		if (endDate.isAfter(currentTime)) {
			throw new InvalidDateRangeException(endDate);
		}
	}

	public void validateDateRangeForPerHour(final LocalDate date) {
		final LocalDate currentTime = LocalDate.now(clock);
		if (date.isBefore(currentTime.minusMonths(3)) || date.isAfter(currentTime)) {
			throw new InvalidDateRangeException(date);
		}
	}

	public void validateDateRangeForPerMinute(final LocalDateTime startTime, final LocalDateTime endTime) {
		final LocalDateTime currentTime = LocalDateTime.now(clock);
		if (startTime.isBefore(currentTime.minusWeeks(1)) || startTime.isAfter(currentTime)) {
			throw new InvalidDateTimeRangeException(startTime);
		}

		if (endTime.isBefore(currentTime.minusWeeks(1)) || endTime.isAfter(currentTime)) {
			throw new InvalidDateTimeRangeException(endTime);
		}

		if (startTime.isAfter(endTime)) {
			throw new StartTimeAfterEndTimeException();
		}
	}
}
