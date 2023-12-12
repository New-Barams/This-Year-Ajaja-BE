package com.newbarams.ajaja.common.support;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.newbarams.ajaja.global.config.QuerydslConfig;

/**
 * Supports JPA Slice Test with Monkey
 * @author hejow
 */
@DataJpaTest
@Import(QuerydslConfig.class)
public abstract class JpaTestSupport extends MonkeySupport {
}
