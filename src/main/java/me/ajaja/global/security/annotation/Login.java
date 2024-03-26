package me.ajaja.global.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.ajaja.global.security.resolver.AuthenticatedUserArgumentResolver;

/**
 * Get User ID from SecurityContext. To use this annotation authorization should be done.
 * Without authorization 401 status can be occurred.
 *
 * <pre>
 * &#064;Authorize
 * &#064;PostMapping("/hello")
 * public AjajaResponse<?> hello(
 * 	&#064;Login Long userId
 * ) {
 * 	// do something...
 * }
 * </pre>
 *
 * @see AuthenticatedUserArgumentResolver
 * @see Authorize
 * @author hejow
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
