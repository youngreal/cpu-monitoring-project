package com.example.terabackendtest.controller.validation;

import java.time.Clock;
import java.time.LocalDateTime;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartTimeValidator implements ConstraintValidator<CheckStartTime, LocalDateTime> {

	private final Clock clock;

	public StartTimeValidator(final Clock clock) {
		this.clock = clock;
	}

	@Override
	public boolean isValid(final LocalDateTime startTime, final ConstraintValidatorContext context) {
		final LocalDateTime currentTime = LocalDateTime.now(clock);
		if (startTime.isBefore(currentTime.minusWeeks(1)) || startTime.isAfter(currentTime)) {
			return false;
		}

		return true;
	}
}

