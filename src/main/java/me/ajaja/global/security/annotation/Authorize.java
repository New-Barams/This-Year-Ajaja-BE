package me.ajaja.global.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.ajaja.global.security.interceptor.AuthorizationInterceptor;

/**
 * This annotation indicates that the API requires authorization
 * <pre>
 * &#064;Authorize
 * &#064;PostMapping("/hello")
 * public AjajaResponse<?> hello() {
 * 	// do something...
 * }
 * </pre>
 *
 * @see AuthorizationInterceptor
 * @author hejow
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
}
