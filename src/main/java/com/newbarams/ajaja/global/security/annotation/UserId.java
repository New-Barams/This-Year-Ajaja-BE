package com.newbarams.ajaja.global.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.newbarams.ajaja.global.security.common.UserAdapter;

/**
 * Custom annotation of @AuthenticationPrincipal with SpEL(Spring Expression Language).
 * This returns User ID which already authenticated from UserAdapter.
 * <p>
 * Should be used with @Authorization
 * @see UserAdapter
 * @see Authorization
 * @author hejow
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : id")
public @interface UserId {
}
