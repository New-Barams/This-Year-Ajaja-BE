package me.ajaja.global.security.resolver;

import java.lang.reflect.Method;
import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.global.security.common.UserAdapter;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Long.class) && parameter.hasParameterAnnotation(Login.class);
	}

	@Override
	public Long resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		validateAuthorized(parameter);
		return parseIdFromContext();
	}

	private void validateAuthorized(MethodParameter parameter) {
		Method method = Objects.requireNonNull(parameter.getMethod());

		if (method.getAnnotation(Authorize.class) == null) {
			throw new AjajaException(ErrorCode.AUTHORIZATION_NOT_PROCESSED);
		}
	}

	private Long parseIdFromContext() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserAdapter userAdapter = (UserAdapter)authentication.getPrincipal();
		return userAdapter.id();
	}
}
