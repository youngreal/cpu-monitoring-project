package com.example.terabackendtest.exception;

import lombok.Getter;

@Getter
public class CpuUsageCollectFailException extends RuntimeException {
	private static final String MESSAGE = "cpu collection failed";

	public CpuUsageCollectFailException() {
		super(MESSAGE);
	}
}
