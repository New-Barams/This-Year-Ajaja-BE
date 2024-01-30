package com.newbarams.ajaja.global.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.newbarams.ajaja.global.security.aspect.AuthorizationAspect;

/**
 * This annotation indicates that the API requires authorization
 * <pre>
 * &#064;Authorization
 * &#064;PostMapping("/hello")
 * public AjajaResponse<*> hello() {
 * 	// do something...
 * }
 * </pre>
 *
 * @see AuthorizationAspect
 * @author hejow
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorization {
}
