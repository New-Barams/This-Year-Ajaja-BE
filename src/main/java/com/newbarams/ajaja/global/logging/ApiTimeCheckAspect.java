package com.newbarams.ajaja.global.logging;

import static com.newbarams.ajaja.global.util.RequestUtil.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class ApiTimeCheckAspect {
	private static final String CONTROLLER_LOGGING_CONDITION = """
		(within(@org.springframework.stereotype.Controller *)
		|| within(@org.springframework.web.bind.annotation.RestController *))
		&& execution(public * *(..))
		""";

	private record Process(Object result, long proceed) {
	}

	@Around(CONTROLLER_LOGGING_CONDITION)
	public Object executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = getRequest();
		Process process = run(joinPoint);
		log.info("[API] Call : {} {}, Processed : {}ms", request.getMethod(), request.getRequestURI(), process.proceed);
		return process.result;
	}

	private Process run(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();

		return new Process(result, stopWatch.getTotalTimeMillis());
	}
}
