package com.example.terabackendtest.exception;

import lombok.Getter;

@Getter
public class InvalidDateRangeException extends RuntimeException {
	private static final String MESSAGE = "cpu collection failed";

	public InvalidDateRangeException() {
		super(MESSAGE);
	}
}
