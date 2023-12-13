package com.newbarams.ajaja.global.aop;

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

@Aspect
@Slf4j
@Component
public class LoggingAspect {
	private static final String CONTROLLER_LOGGING_CONDITION = """
		(within(@org.springframework.stereotype.Controller *)
		|| within(@org.springframework.web.bind.annotation.RestController *))
		&& execution(public * *(..))
		""";

	@Around(CONTROLLER_LOGGING_CONDITION)
	public void executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = extractRequest();
		long proceedTime = countProceedTime(joinPoint);
		log.info("[API] Called : {} {}, Processed : {}ms", request.getMethod(), request.getRequestURI(), proceedTime);
	}

	private HttpServletRequest extractRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return Objects.requireNonNull(attributes).getRequest();
	}

	private long countProceedTime(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		joinPoint.proceed();
		stopWatch.stop();

		return stopWatch.getTotalTimeMillis();
	}
}
