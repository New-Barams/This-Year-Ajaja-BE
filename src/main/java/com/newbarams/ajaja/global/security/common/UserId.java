package com.newbarams.ajaja.global.security.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Custom annotation of @AuthenticationPrincipal with SpEL(Spring Expression Language) <br>
 * This returns User ID which already authenticated from UserAdapter.
 * @author hejow
 * @see UserAdapter
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : id")
public @interface UserId {
}
