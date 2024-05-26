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

// 프론트와 응답포맷이 합의되면, 예외를 관리하고, 공통 형식을 가지는 방식으로 개선
@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final String COMMON_MESSAGE = "message : ";

	@ExceptionHandler(StartTimeAfterEndTimeException.class)
	public ResponseEntity<String> startTimeAfterEndTimeException(StartTimeAfterEndTimeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(COMMON_MESSAGE + e.getMessage());
	}

	@ExceptionHandler(CpuUsageCollectFailException.class)
	public ResponseEntity<String> cpuUsageCollectFailException(CpuUsageCollectFailException e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(COMMON_MESSAGE + e.getMessage());
	}

	@ExceptionHandler(InvalidDateRangeException.class)
	public ResponseEntity<String> invalidDateRangeException(InvalidDateRangeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(COMMON_MESSAGE + e.getMessage());
	}

	@ExceptionHandler(InvalidDateTimeRangeException.class)
	public ResponseEntity<String> invalidDateTimeRangeException(InvalidDateTimeRangeException e) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(COMMON_MESSAGE + e.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> constraintViolationException() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(COMMON_MESSAGE + "잘못된 입력값 입니다");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> methodArgumentTypeMismatchException() {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(COMMON_MESSAGE + "잘못된 입력형식 입니다");
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> runtimeException(RuntimeException e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(COMMON_MESSAGE + e.getMessage());
	}
}
