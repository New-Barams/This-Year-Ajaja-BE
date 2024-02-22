package me.ajaja.common.support;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import me.ajaja.global.config.QuerydslConfig;

/**
 * Supports JPA Slice Test with Monkey
 * @author hejow
 */
@DataJpaTest
@Import(QuerydslConfig.class)
@EnableJpaRepositories(basePackages = "me.ajaja")
@EntityScan(basePackages = "me.ajaja")
public abstract class JpaTestSupport extends MonkeySupport {
}
