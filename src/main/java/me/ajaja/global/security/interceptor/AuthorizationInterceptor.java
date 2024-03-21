package me.ajaja.global.security.interceptor;

import static org.springframework.http.HttpHeaders.*;

import java.lang.reflect.Method;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.global.util.BearerUtil;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
	private final JwtParser jwtParser;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (isAuthorizeRequired(handler)) {
			String token = request.getHeader(AUTHORIZATION);
			BearerUtil.validate(token);
			authorize(BearerUtil.resolve(token));
		}

		return true;
	}

	private boolean isAuthorizeRequired(Object handler) {
		Method method = ((HandlerMethod)handler).getMethod();
		return method.getAnnotation(Authorize.class) != null;
	}

	private void authorize(String jwt) {
		Authentication authentication = jwtParser.parseAuthentication(jwt);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
