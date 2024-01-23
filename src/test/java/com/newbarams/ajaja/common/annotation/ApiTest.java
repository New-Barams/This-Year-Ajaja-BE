package com.newbarams.ajaja.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;

import com.newbarams.ajaja.common.config.UserDetailsSecurityContextFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithUserDetails
@WithSecurityContext(factory = UserDetailsSecurityContextFactory.class)
@Test
public @interface ApiTest {
}
