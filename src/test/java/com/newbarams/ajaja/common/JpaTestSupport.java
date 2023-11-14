package com.newbarams.ajaja.common;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.newbarams.ajaja.global.config.QuerydslConfig;

/**
 * Supporting Jpa Slice Test With Monkey And Querydsl
 * @author hejow
 */
@DataJpaTest
@Import(QuerydslConfig.class)
public abstract class JpaTestSupport extends MonkeySupport {
}
