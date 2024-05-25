package com.example.terabackendtest.controller.validation;

import java.time.Clock;
import java.time.LocalDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EndTimeValidator implements ConstraintValidator<CheckEndTime, LocalDateTime> {

	private final Clock clock;

	public EndTimeValidator(final Clock clock) {
		this.clock = clock;
	}

	@Override
	public boolean isValid(final LocalDateTime endTime, final ConstraintValidatorContext context) {
		final LocalDateTime currentTime = LocalDateTime.now(clock);
		final LocalDateTime oneWeekAgo = currentTime.minusWeeks(1);
		if (endTime.isBefore(oneWeekAgo) || endTime.isAfter(currentTime)) {
			return false;
		}

		return true;
	}
}
