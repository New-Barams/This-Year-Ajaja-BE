package me.ajaja.global.logging;

import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ApiTimeCheckAspect {
	private static final String API_CHECK_FORM = "[{}] URI : {}, {}ms Processed";

	@Around("within(@org.springframework.web.bind.annotation.RestController *)) && execution(public * *(..))")
	public Object checkRequestedApi(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = getRequest();

		var watch = new StopWatch();
		watch.start();

		try {
			return joinPoint.proceed();
		} finally {
			watch.stop();
			log.info(API_CHECK_FORM, request.getMethod(), request.getRequestURI(), watch.getTotalTimeMillis());
		}
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return Objects.requireNonNull(attributes).getRequest();
	}
}
