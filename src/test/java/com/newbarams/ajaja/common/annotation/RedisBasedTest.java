package com.newbarams.ajaja.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.newbarams.ajaja.common.config.TestRedisConfig;

/**
 * Supports Integration Test With Embedded Redis. <br>
 * Must BE CAREFUL About Rollback
 * @author hejow
 */
@Import(TestRedisConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisBasedTest {
}
