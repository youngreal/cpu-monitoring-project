package com.example.terabackendtest.controller.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = StartTimeValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckStartTime {
	String message() default "잘못된 입력조건 입니다";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
