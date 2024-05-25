package com.example.terabackendtest.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.terabackendtest.exception.CpuUsageCollectFailException;
import com.example.terabackendtest.exception.InvalidDateRangeException;
import com.example.terabackendtest.exception.InvalidDateTimeRangeException;
import com.example.terabackendtest.exception.StartTimeAfterEndTimeException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(StartTimeAfterEndTimeException.class)
	public ResponseEntity<String> startTimeAfterEndTimeException(StartTimeAfterEndTimeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}

	@ExceptionHandler(CpuUsageCollectFailException.class)
	public ResponseEntity<String> cpuUsageCollectFailException(CpuUsageCollectFailException e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(e.getMessage());
	}

	@ExceptionHandler(InvalidDateRangeException.class)
	public ResponseEntity<String> invalidDateRangeException(InvalidDateRangeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}

	@ExceptionHandler(InvalidDateTimeRangeException.class)
	public ResponseEntity<String> invalidDateTimeRangeException(InvalidDateTimeRangeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(e.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> constraintViolationException() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body("잘못된 입력값 입니다");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> methodArgumentTypeMismatchException() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body("잘못된 입력형식 입니다");
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> runtimeException(RuntimeException e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(e.getMessage());
	}
}
