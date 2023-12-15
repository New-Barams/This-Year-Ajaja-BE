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

	private record Process(Object result, long proceed) {
	}

	@Around(CONTROLLER_LOGGING_CONDITION)
	public Object executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = extractRequest();
		Process process = run(joinPoint);
		log.info("[API] Call : {} {}, Processed : {}ms", request.getMethod(), request.getRequestURI(), process.proceed);
		return process.result;
	}

	private HttpServletRequest extractRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return Objects.requireNonNull(attributes).getRequest();
	}

	private Process run(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();

		return new Process(result, stopWatch.getTotalTimeMillis());
	}
}
