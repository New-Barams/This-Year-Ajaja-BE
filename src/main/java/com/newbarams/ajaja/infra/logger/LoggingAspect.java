package com.newbarams.ajaja.infra.logger;

import java.time.Instant;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.newbarams.ajaja.global.common.TimeValue;

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
	public Object executeLogging(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		ServletRequestAttributes requestAttributes
			= (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = (requestAttributes).getRequest();

		Map<String, Object> requestParams = createRequestParams(joinPoint, request);

		log.info("request log - {}", requestParams);

		return result;
	}

	private static Map<String, Object> createRequestParams(
		ProceedingJoinPoint joinPoint,
		HttpServletRequest request
	) {
		Map<String, Object> requestParams = new LinkedHashMap<>();
		TimeValue timeValue = new TimeValue(Instant.now());

		try {
			requestParams.put("controller", joinPoint.getSignature().getDeclaringType().getName());
			requestParams.put("methodName", joinPoint.getSignature().getName());
			requestParams.put("requestParams", getParams(request));
			requestParams.put("log_time", timeValue.toLocalDateTime());
			requestParams.put("request_uri", request.getRequestURI());
			requestParams.put("request_http_method", request.getMethod());
		} catch (Exception e) {
			log.error("Controller Request Exception", e);
		}

		return requestParams;
	}

	private static JSONObject getParams(HttpServletRequest request) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		Enumeration<String> params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String param = params.nextElement();
			String replaceParam = param.replaceAll("\\.", "-");
			jsonObject.put(replaceParam, request.getParameter(param));
		}
		return jsonObject;
	}
}
