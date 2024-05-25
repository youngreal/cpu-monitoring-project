package com.example.terabackendtest.exception;

import lombok.Getter;

@Getter
public class StartTimeAfterEndTimeException extends RuntimeException {
	private static final String MESSAGE = "start time cannot exceed the end time";

	public StartTimeAfterEndTimeException() {
		super(MESSAGE);
	}
}
