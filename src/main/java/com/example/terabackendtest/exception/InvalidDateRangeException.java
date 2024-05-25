package com.example.terabackendtest.exception;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class InvalidDateRangeException extends RuntimeException {
	private static final String MESSAGE_FORMAT = "%s is invalid date range";

	public InvalidDateRangeException(final LocalDate localDate) {
		super(String.format(MESSAGE_FORMAT, localDate));
	}
}
