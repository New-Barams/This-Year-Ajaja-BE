package com.newbarams.ajaja.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithSecurityContext;

import com.newbarams.ajaja.common.config.ApiTestSecurityContextFactory;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = ApiTestSecurityContextFactory.class)
@Test
public @interface ApiTest {
}
