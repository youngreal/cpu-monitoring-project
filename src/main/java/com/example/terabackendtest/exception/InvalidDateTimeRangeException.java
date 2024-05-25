package com.example.terabackendtest.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class InvalidDateTimeRangeException extends RuntimeException {
	private static final String MESSAGE_FORMAT = "%s is invalid datetime range";

	public InvalidDateTimeRangeException(final LocalDateTime localDateTime) {
		super(String.format(MESSAGE_FORMAT, localDateTime));
	}
}
